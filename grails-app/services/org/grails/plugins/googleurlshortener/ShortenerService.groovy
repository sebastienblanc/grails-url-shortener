
package org.grails.plugins.googleurlshortener

import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.codehaus.groovy.grails.web.json.JSONArray;

import static groovyx.net.http.ContentType.*
class ShortenerService {
	
	static transactional = true
	def restUri = 'https://www.googleapis.com/urlshortener/v1/'
	URLResource shorten(String url){
		def requestMap = [:]
		if(ConfigurationHolder.config.google?.shortener.api.key){
			requestMap.put 'key', ConfigurationHolder.config.google.shortener.api.key
		}
		withRest(uri: restUri) {
			
			def response = post(
					path: 'url',
					query:requestMap,
					body: [longUrl: url],
					requestContentType: JSON
					
					)
			
			def urlResource = new URLResource(response.data)
			urlResource.shortUrl = response.data.id
			return urlResource
		}
	}
	
	URLResource expand(String url){
		withRest(uri:restUri) {
			def requestMap = ['shortUrl':url]
			if(ConfigurationHolder.config.google?.shortener.api.key){
				requestMap.put 'key', ConfigurationHolder.config.google.shortener.api.key
			}
			def response = get(
					path: 'url',
					query: requestMap,
					requestContentType: JSON
					)
			def urlResource = new URLResource(response.data)
			urlResource.shortUrl = response.data.id
			return urlResource
		}
	}
	
	URLResource getAnalytics(String url){
		def requestMap = ['shortUrl':url,'projection':'FULL']
		if(ConfigurationHolder.config.google?.shortener.api.key){
			requestMap.put 'key', ConfigurationHolder.config.google.shortener.api.key
		}
		withRest(uri: restUri) {
			
			def response = get(
					path: 'url',
					query: requestMap,
					requestContentType: JSON
					)
			def urlResource = new URLResource(response.data)
			Analytics analytics = new Analytics();
			if(response.data.analytics?.allTime){
				analytics.allTime = parseDetails(response.data.analytics?.allTime)
			}
			if(response.data.analytics?.month){
				analytics.month = parseDetails(response.data.analytics?.month)
			}
			if(response.data.analytics?.week){
				analytics.week = parseDetails(response.data.analytics?.week)
			}
			if(response.data.analytics?.day){
				analytics.day = parseDetails(response.data.analytics?.day)
			}
			if(response.data.analytics?.twoHours){
				analytics.twoHours = parseDetails(response.data.analytics?.twoHours)
			}
			urlResource.analytics = analytics
			urlResource.shortUrl = response.data.id
			return urlResource
		}
	}
	def parseDetails(Map source){
		AnalyticsDetails details = new AnalyticsDetails()
		details.shortUrlClicks = source.get('shortUrlClicks') as int
		details.longUrlClicks = source.get('longUrlClicks') as int
		if(source.referrers){
			details.referrers = parseDetailsFragment(source.referrers)
		}
		if(source.countries){
			details.countries = parseDetailsFragment(source.countries)
		}
		if(source.browsers){
			details.browsers = parseDetailsFragment(source.browsers)
		}
		if(source.platforms){
			details.platforms = parseDetailsFragment(source.platforms)
		}
		return details
	}
	def parseDetailsFragment(net.sf.json.JSONArray jsonArray){
		jsonArray.collect{new AnalyticsDetailsFragment(label:it.id,labelCount:it.count as int)}
	}
}
