#Simple Search

The project is divided in 3 parts:

+ Link Collector or Crawler
+ Indexer
+ Search

We use [ElasticSearch](http://elasticsearch.org ) as the search engine and the code, for now, it will be written in [Groovy](http://groovy.codehaus.org/).


## Install & configure ElasticSearch

### Java installation
    sudo apt-get update
    sudo apt-get install openjdk-7-jre-headless -y


### ElasticSearch installation
    wget https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-0.90.0.deb
    sudo dpkg -i elasticsearch-0.90.0.deb
    sudo service elasticsearch start


## Plugins & Rivers

### Mapper Attachment plugin

[Attachment plugin](https://github.com/elasticsearch/elasticsearch-mapper-attachments)

The mapper attachments plugin adds the attachment type to ElasticSearch using Tika.

In order to install the plugin, simply run:
    bin/plugin -install elasticsearch/elasticsearch-mapper-attachments/1.7.0

### Web Interface - Inquisitor
If you want to have a Web interface [ElasticSearch Inquisitor](https://github.com/polyfractal/elasticsearch-inquisitor)

    bin/plugin -install polyfractal/elasticsearch-inquisitor

Once the plugin is installed go to [ES Inquisitor](http://localhost:9200/_plugin/inquisitor)

## FS River

This river plugin helps to index documents from your local file system.

You can find [FS River](https://github.com/dadoonet/fsriver) in github

In order to install the plugin, run:

    bin/plugin -install fr.pilato.elasticsearch.river/fsriver/0.2.0




> Follow me [@ipedrazas](http://twitter.com/ipedrazas).


