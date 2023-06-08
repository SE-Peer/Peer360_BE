import sys
from wordcloud import WordCloud
from konlpy.tag import Okt
from collections import Counter

def generate_word_cloud(email):
    text = open(email + '.txt').read()

    okt = Okt()
    sentences_tag = okt.pos(text)

    noun_adj_list = [word for word, tag in sentences_tag if tag in ['Noun', 'Adjective']]

    counts = Counter(noun_adj_list)
    tags = counts.most_common(40)

    wordcloud = WordCloud(font_path='/Library/Fonts/Arial Unicode.ttf',
                          background_color='white',
                          width=800,
                          height=800).generate_from_frequencies(dict(tags))

    wordcloud.to_file('wordclouds/' + email + '/wordcloud.png')

if __name__ == "__main__":
    email = sys.argv[1]
    generate_word_cloud(email)