# Group 4

## Describing the file system
In the main group folder, there are two main folders, that are relevant for the submission.
 * The most important one, named Submission, contains, all the jupyter notebooks with the code
 * The second, named Data, contains the results and the eventual middle steps.

## Data file description
* rating_similarity.csv - contains the target_article_id and other compared article_id pairs, with theis similarity values. Table is based on the labeled dataset
* merged_data.csv - contains the article_2015 dataset merged with the similarity.csv. Furthermore, the columns 'authors' is cleaned.
* cleaned_articles - contains the purely the data from the articles_2015.csv with the only modification of the authors column, as that was cleaned and standardized.
* cleaned_text.csv - contains merge_data.csv addtitionally a string text column, including the cleaned title, author, paragraph and ressorts.
* top_80.csv - contains the top 80 most relevant words for each article based on TF-IDF
* top_250.csv - contains the top 250 most relevant words for each article based on TF-IDF
* fakeembeddings_top250.pkl - contains the embeddings using Huggingface for the top 250 words
* huggingface_embeddings.pkl - contains the embeddings using Huggingface for the top80 words
* huggingface_top250_embeddings.pkl - contains the embeddings using Huggingface for the top 250 words
* openaiembed.pkl - contains the embeddings using Huggingface for the top80 words
* openaiembed_top250.pkl - contains the embeddings using Huggingface for the top 250 words

## Submission

The submission folder contains multiple notebooks containing code from data exploration right until the more complex ChatGPT implementation.
Additionally it contains the Jupyter Notebook called Demonstrator.ipynb, this file includes the major steps we took. It presents a small demonstrator of the implemented minimum recommendation and the more complex recommendation of ChatGPT. Finally, the evaluation method is described.