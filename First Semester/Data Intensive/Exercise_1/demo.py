from collections import defaultdict
from io import BytesIO
import re
from sys import dont_write_bytecode
from mrjob.job import MRJob
from mrjob.step import MRStep
import json
import operator

WORD_RE = re.compile(r'[\w]+')

delimiters = ['(', ')', ']', '[', ']', '{', '}', '.', '!', '?', ',', ';',':','+','=','-','_','"','`','~','#','@','&','*','%','€','$','§','/','0','1','2','3','4','5','6','7','8','9']

class TextPreprocess(MRJob):
    FILES = ['stopwords.txt']  
    def steps(self):
        return [
            MRStep(mapper=self.mapper_get_words,
                   reducer=self.reducer_count_words)        
        ]

    def mapper_get_words(self, _, line):
        stops = set(i.strip() for i in open('stopwords.txt'))              
        review = json.loads(line)
        for cat in WORD_RE.findall(review['category']):
            for word in WORD_RE.findall(review['reviewText']):
                if len(word) != 0 and word.lower() not in stops and any(x in word for x in delimiters) == False:
                    yield ((cat.lower(), word.lower()), 1)
      
      
    def reducer_count_words(self, cat_word, counts): 
        cat, wrd  = cat_word
        yield cat_word, sum(counts)


if __name__ == '__main__':
    TextPreprocess.run()