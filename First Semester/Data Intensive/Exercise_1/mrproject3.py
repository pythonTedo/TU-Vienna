from collections import defaultdict
import re
from mrjob.job import MRJob
from mrjob.step import MRStep
import json


WORD_RE = re.compile(r'[\w]+')

delimiters = ['(', ')', ']', '[', ']', '{', '}', '.', '!', '?', ',', ';',':','+','=','-','_','"','`','~','#','@','&','*','%','€','$','§','/','0','1','2','3','4','5','6','7','8','9']

class MapReducer(MRJob):
    FILES = ['stopwords.txt']  

    # initializing the steps
    def steps(self):
        return [
            MRStep(mapper=self.mapper_one,
                   reducer=self.reducer_one),
            MRStep(mapper=self.mapper_two,
                   reducer=self.reducer_two),        
        ]

    # we are getting line by line from JSON. and returning ((category, word) num_of_occurance)
    def mapper_one(self, _, line):
        stops = set(i.strip() for i in open('stopwords.txt'))              
        review = json.loads(line)
        for cat in WORD_RE.findall(review['category']):
            for word in WORD_RE.findall(review['reviewText']):
                if len(word) != 0 and word.lower() not in stops and any(x in word for x in delimiters) == False:
                    yield ((cat.lower(), word.lower()), 1)
      
    # Summing (category, word)
    def reducer_one(self, cat_word, counts): 
        cat, wrd  = cat_word
        yield cat_word, sum(counts)
    def mapper_two(self, cat_word, counts):
        cat, wrd = cat_word
        yield (None, (cat, wrd, counts)) 


    # Iterrating for each line from the generator
    def reducer_two(self, key, cat_wrd_counts): 
        self.generatorList= []
        self.finalWords= []
        wordCounts = defaultdict(int)
        CatCounts = defaultdict(int)
        total=0

        for i, v in enumerate(cat_wrd_counts):
            self.generatorList.append(v)
            ct=self.generatorList[i][0]
            wrd=self.generatorList[i][1]
            wordCounts[wrd] += self.generatorList[i][2]
            CatCounts[ct]+=self.generatorList[i][2]
            total+=self.generatorList[i][2]

        count = len(self.generatorList)


        for i in range(0,count):
          ct=self.generatorList[i][0]
          wrd=self.generatorList[i][1]

        # number of words in each category * words in this category / sum of all words  
          val=wordCounts[wrd]*CatCounts[ct]/total
          
        #the value of the number in the category
          values=pow((self.generatorList[i][2]-val),2)/val
          self.generatorList[i][2] = values

        #sorting by category alphabetically and then the value by descending 
        self.generatorList.sort(key=lambda row: (row[0], -row[2]))  


        # Getting the first 75 occurances for each category
        oldct=''
        if count>0:
            oldct=self.generatorList[0][0]
        cntr=0
        s=''
        ct=''
        for i in range(0,count):
            cntr=cntr+1
            ct=self.generatorList[i][0]
            if oldct!=ct:
               cntr=0
               yield "<" + oldct +"> " + (s), ""
               s=''
               oldct=ct
            if cntr<76:
                s = s + self.generatorList[i][1] + ":" +  str(self.generatorList[i][2]) + " "

                if self.generatorList[i][1] not in self.finalWords:
                    self.finalWords.append(self.generatorList[i][1])
                self.finalWords.sort()
            
        if s!='':
            yield "<" + oldct +"> " + (s), ""     
        s=""

        yield ' '.join(map(str, self.finalWords)), ""

if __name__ == '__main__':
    MapReducer.run()