from crypt import methods
from flask import Flask, render_template
import pandas as pd
import json
from sklearn.decomposition import PCA

app = Flask(__name__)

# ensure that we can reload when we change the HTML / JS for debugging
app.config['SEND_FILE_MAX_AGE_DEFAULT'] = 0
app.config['TEMPLATES_AUTO_RELOAD'] = True



@app.route('/', methods=['GET'])
def data():
    # replace this with the real data
    #testData = pd.read_csv("static/data/betterLifeIndex_imputed.csv")
    #pcaData = pd.read_csv("static/data/pca.csv")
    

    # return the index file and the data
    return render_template("index.html")


if __name__ == '__main__':
    app.run(debug=True)
