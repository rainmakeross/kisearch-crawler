//Crawler
var Crawler = require('simplecrawler');

// JSDOM
var jsdom = require("jsdom");
var fs = require("fs");
var jquery = fs.readFileSync("./jquery-2.1.1.min.js", "utf-8");

//NEO4J
var neo4j = require('neo4j');
var db = new neo4j.GraphDatabase('http://192.168.0.104:7474');


var myCrawler = new Crawler("hardwarezone.com.sg", "/", 80, 300);
var index = 0;

myCrawler.on("fetchcomplete",function(queueItem, responseBuffer, response) {
    console.log("I just received %s (%d bytes)",queueItem.url,responseBuffer.length);
    console.log("It was a resource of type %s",response.headers['content-type']);

    // Do something with the data in responseBuffer
    //console.log(responseBuffer.toString('utf8').split(" "));
    if(response.headers['content-type'] === "text/html; charset=utf-8"){

        jsdom.env({
            html: responseBuffer.toString('utf8'),
            src: [jquery],
            done: function (errors, window) {
                var $ = window.$;
                $('script').remove();
                $('noscript').remove();

                wordArr = $('body').text().replace(/\s{2,9999}/g, ' ').split(" ");
                var pageNode = db.createNode({url: queueItem.url});     // instantaneous, but...
                pageNode.save(function (err, node) {    // ...this is what actually persists.
                    if (err) {
                        console.error('Error saving new node to database:', err);
                    } else {
                        console.log('pageNode saved to database with id:', node.id);
                    }
                });
                $('a').each(function(){
                    if(this.href.substring(0,4) === "http"){

                        var linkNode = db.createNode({link: this.href});     // instantaneous, but...
                        linkNode.save(function (err, node) {    // ...this is what actually persists.
                            if (err) {
                                console.error('Error saving new node to database:', err);
                            } else {
                                pageNode.createRelationshipTo(linkNode,'link_to',function(){
                                    //console.log("Link Relationship created");
                                });
                                console.log('linkNode saved to database with id:', node.id);
                            }
                        });

                    }
                })



                wordArr.forEach(function(word){
                    var wordNode = db.createNode({word: word, index: index++});     // instantaneous, but...
                    wordNode.save(function (err, node) {    // ...this is what actually persists.
                        if (err) {
                            console.error('Error saving new node to database:', err);
                        } else {
                            pageNode.createRelationshipTo(wordNode,'contains',function(){
                                console.log("Relationship created");
                            });
                            console.log('wordNode saved to database with id:', node.id);
                        }

                    });
                })

            }
        });

    }


});

myCrawler.on("complete", function(){
    myCrawler.stop();
});

var conditionID = myCrawler.addFetchCondition(function(parsedURL) {
    return !parsedURL.path.match(/\.(pdf|png|gif|jpg|css|js|ico)$/i);
});

var concurrency = myCrawler.maxConcurrency = 25;

var timeOut = myCrawler.timeout = 10000;

myCrawler.start();





