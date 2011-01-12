package org.grails.plugins.googleurlshortener

class URLResource {
	String kind
	String shortUrl
	String longUrl
	String status
	String created
	Analytics analytics
    static constraints = {
    }
}
