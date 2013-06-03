#Simple Search

The project is divided in 3 parts:

+ Link Collector or Crawler
+ Indexer
+ Search

We use [ElasticSearch](http://elasticsearch.org ) as the search engine and the code, for now, it will be written in [Groovy](http://groovy.codehaus.org/).


## Install & configure ElasticSearch

Installing ElasticSearch is quiet easy (at least in Linux). As a database we will be using [MongoDB](http://mongodb.org). If you wonder why on earth I'm using MongoDB instead a classic RDBMS the answer is quite simple: because if we use Mongo we can add the mongodb river example, and using a RDBMS we will not have that (that's right, so far, there's only PUSH with RDBMS, and if you're wondering why, the answer is Delete: how to handle delet entries).

### Pre-requisties:

+ Elastic Search
+ MongoDB

### Website to index
Searching is meaningless unless you have content. Content is King, that we would say in the CMS world where I come from. I've created a small website that replicates my local documentation site (After indexing, I will remove all the PDFs from O'Reilly to avoid issues with copyrighted material).

We will be indexing HTML and PDF. Indexing HTML is quite trivial, since it's text and ElasticSearch loves text. PDFs are slightly different. To index them, we're going to use the Attachment Plugin (see below). This plugin uses [Apache Tika](http://tika.apache.org/), so, in principle, you could index any document type supported by Apache Tika.

The website we will be indexing is [http://simplesearch.pedrazas.me](http://simplesearch.pedrazas.me) that contains around 270MB of data


### Java installation in Ubuntu
    sudo apt-get update
    sudo apt-get install openjdk-7-jre-headless -y


### ElasticSearch installation in Linux
    wget https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-0.90.0.deb
    sudo dpkg -i elasticsearch-0.90.0.deb

    Installed at /usr/share/elasticsearch



## Indexing

To add documents to our index we will explore three different paths:

+ Classic a-la-google using a web spider or crawler
+ Using the FS River, which means adding the files located at some directory in our filesystem
+ Using the MongoDB River

### Mapper Attachment plugin

[Attachment plugin](https://github.com/elasticsearch/elasticsearch-mapper-attachments)

The mapper attachments plugin adds the attachment type to ElasticSearch using Tika.

In order to install the plugin, simply run:

    sudo bin/plugin -install elasticsearch/elasticsearch-mapper-attachments/1.7.0

### Web Interface - Inquisitor
If you want to have a Web interface [ElasticSearch Inquisitor](https://github.com/polyfractal/elasticsearch-inquisitor)

    sudo bin/plugin -install polyfractal/elasticsearch-inquisitor

Once the plugin is installed go to [ES Inquisitor](http://localhost:9200/_plugin/inquisitor)

## FS River

This river plugin helps to index documents from your local file system.

You can find [FS River](https://github.com/dadoonet/fsriver) in github

In order to install the plugin, run:

    sudo bin/plugin -install fr.pilato.elasticsearch.river/fsriver/0.2.0

## MongoDB River

You can find [FS River](https://github.com/richardwilly98/elasticsearch-river-mongodb/) in github

    sudo bin/plugin -url https://github.com/downloads/richardwilly98/elasticsearch-river-mongodb/elasticsearch-river-mongodb-1.6.5.zip -install river-mongodb


## One last thing...
Remember to restart ES after installing these plugins



> Follow me [@ipedrazas](http://twitter.com/ipedrazas).


