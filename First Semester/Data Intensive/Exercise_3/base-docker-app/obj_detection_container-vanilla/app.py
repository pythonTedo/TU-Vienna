# Lint as: python3
# Copyright 2019 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

from asyncore import write
from PIL import Image
from PIL import ImageDraw
import os
import time
import numpy as np
import io
from io import BytesIO
from flask import Flask, request, Response, jsonify
import tensorflow_hub as hub
import tensorflow as tf
import json

#module_handle = "https://tfhub.dev/google/faster_rcnn/openimages_v4/inception_resnet_v2/1"
module_handle = "https://tfhub.dev/google/openimages_v4/ssd/mobilenet_v2/1"
detector = hub.load(module_handle).signatures['default']

inf_time = []

# def detection_loop(filename_image):
#   inference_time = 0
#   count = 0
#   borders_selected = {}

#   for key, img in filename_image.items():
#     #print(filename_image)
#     try:
#       #converted_img  = tf.image.convert_image_dtype(img, tf.float32)[tf.newaxis, ...]
#       converted_img = tf.image.decode_image(img, dtype=tf.float32)[tf.newaxis, ...]
#       start_time = time.time()
#       result = detector(converted_img)
#       end_time = time.time()

#       result = {key:value.numpy() for key,value in result.items()}

#       inference_time += end_time - start_time
      
#       print("Found %d objects." % len(result["detection_scores"]))
#       print("Inference time: ", end_time-start_time)

#       borders = result['detection_boxes']
#       classes = result["detection_class_entities"]
#       scores = result["detection_scores"]

#       borders_var = []
#       for i, b in enumerate(borders):
#           if scores[i] >= 0.2:
#               borders_var.append((b.tolist(), classes[i].decode("utf-8")))

#       borders_selected[key] = {"borders and classes": borders_var}
#       count += 1
#       print(count)
#     except:
#       continue
  
#   print("inf time all ", inference_time)
#   print(len(filename_image))
#   avg_inference = inference_time/len(filename_image)
#   inf_time.append(avg_inference)
#   print(avg_inference)
#   return borders_selected
#initializing the flask app

def detection_loop(image):
  inference_time = 0
  borders_selected = {}

  converted_img = tf.image.decode_image(image, dtype=tf.float32)[tf.newaxis, ...]
  start_time = time.time()
  result = detector(converted_img)
  end_time = time.time()

  result = {key:value.numpy() for key,value in result.items()}

  inference_time = end_time - start_time

  print("Found %d objects." % len(result["detection_scores"]))
  print("Inference time: ", end_time-start_time)

  borders = result['detection_boxes']
  classes = result["detection_class_entities"]
  scores = result["detection_scores"]

  borders_var = []
  for i, b in enumerate(borders):
      if scores[i] >= 0.2:
          borders_var.append((b.tolist(), classes[i].decode("utf-8")))

  borders_selected = {"borders and classes": borders_var}
 
  return borders_selected, inference_time



app = Flask(__name__)

#routing http posts to this method
@app.route('/api/detect', methods=['POST', 'GET'])
def main():
  #img = request.files["image"].read()
  #image = Image.open(io.BytesIO(img))
  #data_input = request.args['input']
  data_input = request.values.get('input')
  #output = request.values.get('output')
  #output = request.form.get('output')

  path = data_input
  borders_selected = {}
  filename_image = {}
  
  input_format = ["jpg", "png", "jpeg"]
  incoming_data = data_input.split(".")

  def img_generator(images_path):
    for file_name in os.listdir(images_path)[20000:]:
      yield (file_name, tf.io.read_file(images_path + file_name))

  if incoming_data[-1] in input_format:
    #print("It is file and with correct format")
    filename = data_input.split("/")[-1]
    filename_image[filename] = Image.open(data_input)
    path = os.path.dirname(data_input)+"/"
  else:
      #print(data_input + " is a path with the following files: ")
      counter = 0
      loop = 0
      last_file = os.listdir(data_input)[-1]
#############################################################################
      inference_time = []
      counter = 0
      generated_images = img_generator(data_input)
      with open("demo.txt","w") as fo:
        for filename, image in generated_images:
          counter += 1
          print(counter)
          try:
            borders, inf_time = detection_loop(image)
            inference_time.append(inf_time)
            fo.write(str(borders))
          except:
            continue
        fo.write(str(np.average(inference_time)))

      ###############################################################
      # for filename in os.listdir(data_input):
      #     image_path = data_input + filename
          #print("our image path", image_path)
          #filename_image[filename] = Image.open(image_path)
          ###############################################################
          # if (counter < 6500): #6500
          #   counter += 1
          #   print(last_file)
          #   print(counter)
          #   filename_image[filename] = tf.io.read_file(image_path)
          # if (counter == 6500 or last_file==filename):
          #   loop += 1 
          #   borders_selected = detection_loop(filename_image)
          #   fo = open(f"object_detections_loop_{loop}.txt", "w")
          #   fo.writelines(str(borders_selected))
          #   fo.writelines(str(inf_time))
          #   fo.close()
          #   del filename_image
          #   del borders_selected
          #   filename_image = {}
          #   counter = 0
          ###############################################################
          
          #print("  " + filename)
  
  #detections = detection_loop(filename_image)

  status_code = Response(status = 200)
  return status_code
  json.dumps(detections)
# image=cv2.imread(args.input)
# image=cv2.cvtColor(image,cv2.COLOR_BGR2RGB)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
