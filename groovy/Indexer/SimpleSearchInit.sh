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
            "_source" : { "enabled" : true , "excludes": ["webpage.content"]},
            "properties" : {
                "url" : { "type" : "string", "index" : "not_analyzed" },
                "content_type" : { "type" : "string", "index" : "not_analyzed" },
                "content" : {
                    "type" : "attachment",
                    "fields" : {
                        "file" : {"index" : "yes", "store" : "no"},
                        "date" : {"store" : "yes"},
                        "author" : {"store" : "yes"},
                        "title" : {"store"  : "yes"}
                    }
                }
            }
        }
    }
}'


curl -XPUT 'localhost:9200/simple/' -d '{
            "mappings" : {
                  "webpage" : {
                    "_source" : { "enabled" : false },
                    "properties" : {
                      "file" : {
                        "type" : "attachment",
                        "path" : "full",
                        "fields" : {
                          "file" : {
                            "type" : "string",
                            "store" : "yes",
                            "term_vector" : "with_positions_offsets"
                          },
                          "author" : {
                            "type" : "string"
                          },
                          "title" : {
                            "type" : "string",
                            "store" : "yes"
                          },
                          "name" : {
                            "type" : "string",
                            "store" : "yes"
                          },
                          "date" : {
                            "type" : "date",
                            "format" : "dateOptionalTime"
                          },
                          "keywords" : {
                            "type" : "string"
                          },
                          "content_type" : {
                            "type" : "string",
                            "store" : "yes"
                          }
                        }
                      },
                      "name" : {
                        "type" : "string",
                        "analyzer" : "keyword",
                        "store" : true
                      },
                      "postDate" : {
                        "type" : "date",
                        "format" : "dateOptionalTime"
                      },
                      "filesize" : {
                        "type" : "long"
                      },
                      "url" : {
                        "type" : "string",
                        "store" : true
                      },
                       "content_type" : {
                        "type" : "string",
                        "store" : true
                      }
                    }
                  }
            }
        }'


curl -XDELETE localhost:9200/fsindex
curl -XDELETE localhost:9200/_river/fsindex

curl -XPUT 'localhost:9200/fsindex/' -d '{
            "mappings" : {
                  "doc" : {
                    "_source" : { "enabled" : false },
                    "properties" : {
                      "file" : {
                        "type" : "attachment",
                        "path" : "full",
                        "fields" : {
                          "file" : {
                            "type" : "string",
                            "store" : "yes",
                            "term_vector" : "with_positions_offsets"
                          },
                          "author" : {
                            "type" : "string"
                          },
                          "title" : {
                            "type" : "string",
                            "store" : "yes"
                          },
                          "name" : {
                            "type" : "string",
                            "store" : "yes"
                          },
                          "date" : {
                            "type" : "date",
                            "format" : "dateOptionalTime"
                          },
                          "keywords" : {
                            "type" : "string"
                          },
                          "content_type" : {
                            "type" : "string",
                            "store" : "yes"
                          }
                        }
                      },
                      "name" : {
                        "type" : "string",
                        "analyzer" : "keyword",
                        "store" : true
                      },
                      "pathEncoded" : {
                        "type" : "string",
                        "analyzer" : "keyword"
                      },
                      "postDate" : {
                        "type" : "date",
                        "format" : "dateOptionalTime"
                      },
                      "rootpath" : {
                        "type" : "string",
                        "analyzer" : "keyword"
                      },
                      "virtualpath" : {
                        "type" : "string",
                        "analyzer" : "keyword",
                        "store" : true
                      },
                      "filesize" : {
                        "type" : "long"
                      }
                    }
                  }
            }
        }'

curl -XPUT 'localhost:9200/_river/fsindex/_meta' -d '{
  "type": "fs",
  "fs": {
        "name": "SimpleSearch website",
        "url": "/home/ivan/www/simplesearch",
        "update_rate": 3600000,
        "includes": "*.html,*.pdf"
  }
}'


curl -XGET http://localhost:9200/fsindex/doc/_search -d '{
  "query" : {
    "text" : {
        "_all" : "main Index and MongoDB"
    }
  }
}'

## MongoDB River

curl -X PUT localhost:9200/_river/mongodb/_meta -d '{
        "type":"mongodb",
        "mongodb":{
                "db":"DATABASE_NAME",
                "collection":"COLLECTION",
                "index":"ES_INDEX_NAME"
                    }
            }'


## Start & Stop Rivers
# Start
curl 'localhost:9200/_river/fsindex/_start'
# Stop
curl 'localhost:9200/_river/fsindex/_stop'


# Adding a doc

curl -XPUT 'http://localhost:9200/simple/webpage/1' -d '{
    "webpage" : {
        "url" : "http://localhost/test.html",
        "content_type" : "text/html",
        "content" : "U3RpY2t5IExvY2FsIFRlY2ggRG9jcy4gUGluIGEgZml4ZWQtaGVpZ2h0IGZvb3RlciB0byB0aGUgYm90dG9tIG9mIHRoZSB2aWV3cG9ydCBpbiBkZXNrdG9wIGJyb3dzZXJzIHdpdGggdGhpcyBjdXN0b20gSFRNTCBhbmQgQ1NTLiBBIGZpeGVkIG5hdmJhciBoYXMgYmVlbiBhZGRlZCB3aXRoaW4gI3dyYXAgd2l0aCBwYWRkaW5nLXRvcDogNjBweDsgb24gdGhlIC5jb250YWluZXIuQmFjayB0byBaZW5CdWcgSG9tZSB0byBhY2Nlc3MgbG9jYWwgYXBwcw=="
    }
}'


curl -XPUT 'http://localhost:9200/simple/webpage/2' -d '{
    "webpage" : {
        "url" : "http://localhost/test2.html",
        "content_type" : "text/html",
        "content" : "This sis weird"
    }
}'




curl -XPUT 'http://localhost:9200/simple/webpage/1' -d '{
    "webpage":{
        "url":"http://localhost/test.html",
        "contentType":"text/html",
        "file":"PCFET0NUWVBFIGh0bWw+CjxodG1sIGxhbmc9ImVuIj4KICA8aGVhZD4KICAgIDxtZXRhIGNoYXJz\r\nZXQ9InV0Zi04Ij4KICAgIDx0aXRsZT5Mb2NhbERvY3M8L3RpdGxlPgogICAgPG1ldGEgbmFtZT0i\r\ndmlld3BvcnQiIGNvbnRlbnQ9IndpZHRoPWRldmljZS13aWR0aCwgaW5pdGlhbC1zY2FsZT0xLjAi\r\nPgogICAgPG1ldGEgbmFtZT0iZGVzY3JpcHRpb24iIGNvbnRlbnQ9IiI+CiAgICA8bWV0YSBuYW1l\r\nPSJhdXRob3IiIGNvbnRlbnQ9IiI+CgogICAgPCEtLSBDU1MgLS0+CiAgICA8bGluayBocmVmPSJh\r\nc3NldHMvY3NzL2Jvb3RzdHJhcC5jc3MiIHJlbD0ic3R5bGVzaGVldCI+CiAgICA8c3R5bGUgdHlw\r\nZT0idGV4dC9jc3MiPgoKICAgICAgLyogU3RpY2t5IGZvb3RlciBzdHlsZXMKICAgICAgLS0tLS0t\r\nLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0gKi8KCiAgICAgIGh0\r\nbWwsCiAgICAgIGJvZHkgewogICAgICAgIGhlaWdodDogMTAwJTsKICAgICAgICAvKiBUaGUgaHRt\r\nbCBhbmQgYm9keSBlbGVtZW50cyBjYW5ub3QgaGF2ZSBhbnkgcGFkZGluZyBvciBtYXJnaW4uICov\r\nCiAgICAgIH0KCiAgICAgIC8qIFdyYXBwZXIgZm9yIHBhZ2UgY29udGVudCB0byBwdXNoIGRvd24g\r\nZm9vdGVyICovCiAgICAgICN3cmFwIHsKICAgICAgICBtaW4taGVpZ2h0OiAxMDAlOwogICAgICAg\r\nIGhlaWdodDogYXV0byAhaW1wb3J0YW50OwogICAgICAgIGhlaWdodDogMTAwJTsKICAgICAgICAv\r\nKiBOZWdhdGl2ZSBpbmRlbnQgZm9vdGVyIGJ5IGl0J3MgaGVpZ2h0ICovCiAgICAgICAgbWFyZ2lu\r\nOiAwIGF1dG8gLTYwcHg7CiAgICAgIH0KCiAgICAgIC8qIFNldCB0aGUgZml4ZWQgaGVpZ2h0IG9m\r\nIHRoZSBmb290ZXIgaGVyZSAqLwogICAgICAjcHVzaCwKICAgICAgI2Zvb3RlciB7CiAgICAgICAg\r\naGVpZ2h0OiA2MHB4OwogICAgICB9CiAgICAgICNmb290ZXIgewogICAgICAgIGJhY2tncm91bmQt\r\nY29sb3I6ICNmNWY1ZjU7CiAgICAgIH0KCiAgICAgIC8qIExhc3RseSwgYXBwbHkgcmVzcG9uc2l2\r\nZSBDU1MgZml4ZXMgYXMgbmVjZXNzYXJ5ICovCiAgICAgIEBtZWRpYSAobWF4LXdpZHRoOiA3Njdw\r\neCkgewogICAgICAgICNmb290ZXIgewogICAgICAgICAgbWFyZ2luLWxlZnQ6IC0yMHB4OwogICAg\r\nICAgICAgbWFyZ2luLXJpZ2h0OiAtMjBweDsKICAgICAgICAgIHBhZGRpbmctbGVmdDogMjBweDsK\r\nICAgICAgICAgIHBhZGRpbmctcmlnaHQ6IDIwcHg7CiAgICAgICAgfQogICAgICB9CgoKCiAgICAg\r\nIC8qIEN1c3RvbSBwYWdlIENTUwogICAgICAtLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0t\r\nLS0tLS0tLS0tLS0tLS0tLS0tLSAqLwogICAgICAvKiBOb3QgcmVxdWlyZWQgZm9yIHRlbXBsYXRl\r\nIG9yIHN0aWNreSBmb290ZXIgbWV0aG9kLiAqLwoKICAgICAgI3dyYXAgPiAuY29udGFpbmVyIHsK\r\nICAgICAgICBwYWRkaW5nLXRvcDogNjBweDsKICAgICAgfQogICAgICAuY29udGFpbmVyIC5jcmVk\r\naXQgewogICAgICAgIG1hcmdpbjogMjBweCAwOwogICAgICB9CgogICAgICBjb2RlIHsKICAgICAg\r\nICBmb250LXNpemU6IDgwJTsKICAgICAgfQoKICAgIDwvc3R5bGU+CiAgICA8bGluayBocmVmPSJh\r\nc3NldHMvY3NzL2Jvb3RzdHJhcC1yZXNwb25zaXZlLmNzcyIgcmVsPSJzdHlsZXNoZWV0Ij4KCiAg\r\nICA8IS0tIEhUTUw1IHNoaW0sIGZvciBJRTYtOCBzdXBwb3J0IG9mIEhUTUw1IGVsZW1lbnRzIC0t\r\nPgogICAgPCEtLVtpZiBsdCBJRSA5XT4KICAgICAgPHNjcmlwdCBzcmM9ImFzc2V0cy9qcy9odG1s\r\nNXNoaXYuanMiPjwvc2NyaXB0PgogICAgPCFbZW5kaWZdLS0+CgogICAgPCEtLSBGYXYgYW5kIHRv\r\ndWNoIGljb25zIC0tPgogICAgPGxpbmsgcmVsPSJhcHBsZS10b3VjaC1pY29uLXByZWNvbXBvc2Vk\r\nIiBzaXplcz0iMTQ0eDE0NCIgaHJlZj0iYXNzZXRzL2ljby9hcHBsZS10b3VjaC1pY29uLTE0NC1w\r\ncmVjb21wb3NlZC5wbmciPgogICAgPGxpbmsgcmVsPSJhcHBsZS10b3VjaC1pY29uLXByZWNvbXBv\r\nc2VkIiBzaXplcz0iMTE0eDExNCIgaHJlZj0iYXNzZXRzL2ljby9hcHBsZS10b3VjaC1pY29uLTEx\r\nNC1wcmVjb21wb3NlZC5wbmciPgogICAgICA8bGluayByZWw9ImFwcGxlLXRvdWNoLWljb24tcHJl\r\nY29tcG9zZWQiIHNpemVzPSI3Mng3MiIgaHJlZj0iYXNzZXRzL2ljby9hcHBsZS10b3VjaC1pY29u\r\nLTcyLXByZWNvbXBvc2VkLnBuZyI+CiAgICAgICAgICAgICAgICAgICAgPGxpbmsgcmVsPSJhcHBs\r\nZS10b3VjaC1pY29uLXByZWNvbXBvc2VkIiBocmVmPSJhc3NldHMvaWNvL2FwcGxlLXRvdWNoLWlj\r\nb24tNTctcHJlY29tcG9zZWQucG5nIj4KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg\r\nICA8bGluayByZWw9InNob3J0Y3V0IGljb24iIGhyZWY9ImFzc2V0cy9pY28vZmF2aWNvbi5wbmci\r\nPgogIDwvaGVhZD4KCiAgPGJvZHk+CgoKICAgIDwhLS0gUGFydCAxOiBXcmFwIGFsbCBwYWdlIGNv\r\nbnRlbnQgaGVyZSAtLT4KICAgIDxkaXYgaWQ9IndyYXAiPgoKICAgICAKCiAgICAgIDwhLS0gQmVn\r\naW4gcGFnZSBjb250ZW50IC0tPgogICAgICA8ZGl2IGNsYXNzPSJjb250YWluZXIiPgogICAgICAg\r\nIDxkaXYgY2xhc3M9InBhZ2UtaGVhZGVyIj4KICAgICAgICAgIDxoMT5TdGlja3kgTG9jYWwgVGVj\r\naCBEb2NzPC9oMT4KICAgICAgICA8L2Rpdj4KICAgICAgICA8cCBjbGFzcz0ibGVhZCI+UGluIGEg\r\nZml4ZWQtaGVpZ2h0IGZvb3RlciB0byB0aGUgYm90dG9tIG9mIHRoZSB2aWV3cG9ydCBpbiBkZXNr\r\ndG9wIGJyb3dzZXJzIHdpdGggdGhpcyBjdXN0b20gSFRNTCBhbmQgQ1NTLiBBIGZpeGVkIG5hdmJh\r\nciBoYXMgYmVlbiBhZGRlZCB3aXRoaW4gPGNvZGU+I3dyYXA8L2NvZGU+IHdpdGggPGNvZGU+cGFk\r\nZGluZy10b3A6IDYwcHg7PC9jb2RlPiBvbiB0aGUgPGNvZGU+LmNvbnRhaW5lcjwvY29kZT4uPC9w\r\nPgogICAgICAgIDxwPkJhY2sgdG8gPGEgaHJlZj0iLi9zdGlja3ktZm9vdGVyLmh0bWwiPlplbkJ1\r\nZyBIb21lPC9hPiB0byBhY2Nlc3MgbG9jYWwgYXBwczwvcD4KICAgICAgPC9kaXY+CgogICAgICA8\r\nZGl2IGlkPSJwdXNoIj48L2Rpdj4KICAgIDwvZGl2PgoKICAgIDxkaXYgaWQ9ImZvb3RlciI+CiAg\r\nICAgIDxkaXYgY2xhc3M9ImNvbnRhaW5lciI+CiAgICAgICAgPHAgY2xhc3M9Im11dGVkIGNyZWRp\r\ndCI+TXIgSGVkZ2Vob2cgYWthIDxhIGhyZWY9Imh0dHA6Ly9pdmFuLnBlZHJhemFzLm1lIj5JdmFu\r\nIFBlZHJhemFzPC9hPi48L3A+CiAgICAgIDwvZGl2PgogICAgPC9kaXY+CgoKCiAgICA8IS0tIExl\r\nIGphdmFzY3JpcHQKICAgID09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09\r\nPT09PT09PT09IC0tPgogICAgPCEtLSBQbGFjZWQgYXQgdGhlIGVuZCBvZiB0aGUgZG9jdW1lbnQg\r\nc28gdGhlIHBhZ2VzIGxvYWQgZmFzdGVyIC0tPgogICAgPHNjcmlwdCBzcmM9ImFzc2V0cy9qcy9q\r\ncXVlcnkuanMiPjwvc2NyaXB0PgogICAgPHNjcmlwdCBzcmM9ImFzc2V0cy9qcy9ib290c3RyYXAt\r\ndHJhbnNpdGlvbi5qcyI+PC9zY3JpcHQ+CiAgICA8c2NyaXB0IHNyYz0iYXNzZXRzL2pzL2Jvb3Rz\r\ndHJhcC1hbGVydC5qcyI+PC9zY3JpcHQ+CiAgICA8c2NyaXB0IHNyYz0iYXNzZXRzL2pzL2Jvb3Rz\r\ndHJhcC1tb2RhbC5qcyI+PC9zY3JpcHQ+CiAgICA8c2NyaXB0IHNyYz0iYXNzZXRzL2pzL2Jvb3Rz\r\ndHJhcC1kcm9wZG93bi5qcyI+PC9zY3JpcHQ+CiAgICA8c2NyaXB0IHNyYz0iYXNzZXRzL2pzL2Jv\r\nb3RzdHJhcC1zY3JvbGxzcHkuanMiPjwvc2NyaXB0PgogICAgPHNjcmlwdCBzcmM9ImFzc2V0cy9q\r\ncy9ib290c3RyYXAtdGFiLmpzIj48L3NjcmlwdD4KICAgIDxzY3JpcHQgc3JjPSJhc3NldHMvanMv\r\nYm9vdHN0cmFwLXRvb2x0aXAuanMiPjwvc2NyaXB0PgogICAgPHNjcmlwdCBzcmM9ImFzc2V0cy9q\r\ncy9ib290c3RyYXAtcG9wb3Zlci5qcyI+PC9zY3JpcHQ+CiAgICA8c2NyaXB0IHNyYz0iYXNzZXRz\r\nL2pzL2Jvb3RzdHJhcC1idXR0b24uanMiPjwvc2NyaXB0PgogICAgPHNjcmlwdCBzcmM9ImFzc2V0\r\ncy9qcy9ib290c3RyYXAtY29sbGFwc2UuanMiPjwvc2NyaXB0PgogICAgPHNjcmlwdCBzcmM9ImFz\r\nc2V0cy9qcy9ib290c3RyYXAtY2Fyb3VzZWwuanMiPjwvc2NyaXB0PgogICAgPHNjcmlwdCBzcmM9\r\nImFzc2V0cy9qcy9ib290c3RyYXAtdHlwZWFoZWFkLmpzIj48L3NjcmlwdD4KCiAgPC9ib2R5Pgo8\r\nL2h0bWw+"
    }
}'

# Query

curl -XGET 'http://localhost:9200/simple/webpage/_search?size=10&pretty' -d '{"query": {"query_string":{"query": "sticky"}}}'
curl -XGET 'http://localhost:9200/fsindex/doc/_search?size=10&pretty' -d '{"query": {"query_string":{"query": "sticky"}}}'

curl -XGET 'http://localhost:9200/simple/webpage/_search?size=10&pretty' -d '{"query": {"query_string":{"query": "javascript"}}}'

curl -XGET 'http://localhost:9200/simple/webpage/_search?size=10&pretty' -d '{"query": {"query_string":{"query": "outputStartTag"}}}'

curl -XPOST http://localhost:9200/fsindex/doc/_search -d '{
  "fields" : ["file.content_type", "name", "virtualpath"],
  "query":{
    "match_all" : {}
  }
}'


curl -XPOST http://localhost:9200/fsindex/doc/_search -d '{
  "fields" : ["file"],
  "query":{
    "match_all" : {}
  }
}'


curl -XGET 'http://localhost:9200/simple/webpage/_search?size=10&pretty&q=sticky&fields=url,file.title'
curl -XGET 'http://localhost:9200/fsindex/doc/_search?size=10&pretty&q=sticky&fields=name,virtualpath'


curl -XGET 'http://localhost:9200/fsindex/doc/_search?size=10&pretty&q=sticky&fields=*'
curl -XGET 'http://localhost:9200/simple/webpage/_search?size=10&pretty&q=sticky&fields=*'


### Quick test

curl -XPUT 'http://localhost:9200/paper'

curl -XPUT 'http://localhost:9200/paper/article/1' -d '{
    "article" : {
        "user" : "ipedrazas",
        "message" : "trying out Elastic Search"
    }
}'

curl -XPUT 'http://localhost:9200/paper/article/2' -d '{
    "article" : {
        "user" : "valgreens",
        "message" : "Have you tried backbone.js ?"
    }
}'


#Get document by id
curl -XGET 'http://localhost:9200/paper/article/1'

#Search document
curl -XGET 'http://localhost:9200/paper/article/_search?q=user:ipedrazas'
