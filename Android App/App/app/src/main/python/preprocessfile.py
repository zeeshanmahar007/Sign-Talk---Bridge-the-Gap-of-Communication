# sepcial character (regular expression)
import regex
import nltk
nltk.download('punkt')
from nltk.util import clean_html

# word tokenize
from nltk import word_tokenize

# for stemming
from nltk.stem.porter import PorterStemmer

def preprocess(raw_text):
    raw_text = [raw_text]
    temp1 = []
    temp2 = []

    # convert all text to lower case
    for words in raw_text:
        temp1.append(str.lower(words))

    # tokenize
    temp2 = []
    for i in temp1:
        temp2.append(word_tokenize(i))

    # sepcial character removel (regular expression)
    temp1 = []
    for words in temp2:
        clean = []
        for w in words:
            res = regex.sub(r'[^\w\s]',"",w)
            if res!="":
                clean.append(res)
        temp1.append(clean)

    # apply stemming to remove refix (reduce word into root)
    temp2 = []
    port = PorterStemmer()
    for words in temp1:
        w = []
        for word in words:
            w.append(port.stem(word))
        temp2.append(w)
    return temp2