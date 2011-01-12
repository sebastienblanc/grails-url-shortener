package org.grails.plugins.googleurlshortener


class AnalyticsDetails {
	int shortUrlClicks
	int longUrlClicks
	List referrers
	List countries
	List browsers
	List platforms
	
    static constraints = {
    }
}
