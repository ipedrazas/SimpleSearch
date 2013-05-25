# Delete index



curl -XDELETE localhost:9200/simple

# Index creation


curl -XPOST localhost:9200/simple -d '{
    "settings" : {
        "number_of_shards" : 1,
        "number_of_replicas":0
    },
    "mappings" : {
        "webpage" : {
            "_source" : { "enabled" : true , "excludes": ["webpage.file"]},
            "properties" : {
                "url" : { "type" : "string", "index" : "not_analyzed" },
                "content_type" : { "type" : "string", "index" : "not_analyzed" },

                "file" : {
                    "type" : "attachment",
                    "fields" : {
                        "file" : {"index" : "no", "store" : "no"},
                        "date" : {"store" : "yes"},
                        "author" : {"store" : "yes"},
                        "title" : {"store"  : "yes"}
                    }
                }
            }
        }
    }
}'


# Adding a doc

curl -XPUT 'http://localhost:9200/simple/webpage/1' -d '{
    "webpage" : {
        "url" : "http://localhost/docs/test.html",
        "content_type" : "text/html",
        "file" : "U3RpY2t5IExvY2FsIFRlY2ggRG9jcy4gUGluIGEgZml4ZWQtaGVpZ2h0IGZvb3RlciB0byB0aGUgYm90dG9tIG9mIHRoZSB2aWV3cG9ydCBpbiBkZXNrdG9wIGJyb3dzZXJzIHdpdGggdGhpcyBjdXN0b20gSFRNTCBhbmQgQ1NTLiBBIGZpeGVkIG5hdmJhciBoYXMgYmVlbiBhZGRlZCB3aXRoaW4gI3dyYXAgd2l0aCBwYWRkaW5nLXRvcDogNjBweDsgb24gdGhlIC5jb250YWluZXIuQmFjayB0byBaZW5CdWcgSG9tZSB0byBhY2Nlc3MgbG9jYWwgYXBwcw=="
    }
}'


curl -XPUT 'http://localhost:9200/simple/webpage/2' -d '{
    "webpage" : {
        "url" : "http://localhost/docs/test.html",
        "content_type" : "text/html",
        "content" : "This sis weird"
    }
}'


# Query

curl -XGET 'http://localhost:9200/simple/webpage/_search?size=10&pretty' -d '{"query": {"query_string":{"query": "desktop"}}}'

curl -XGET 'http://localhost:9200/simple/webpage/_search?size=10&pretty' -d '{"query": {"query_string":{"query": "thrift"}}}'
