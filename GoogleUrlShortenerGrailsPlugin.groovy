import org.grails.plugins.googleurlshortener.ShortenerService;

class GoogleUrlShortenerGrailsPlugin {
	// the plugin version
	def version = "0.1"
	// the version or versions of Grails the plugin is designed for
	def grailsVersion = "1.3.4 > *"
	// the other plugins this plugin depends on
	def dependsOn = [rest:"0.6"]
	// resources that are excluded from plugin packaging
	def pluginExcludes = [
		"grails-app/views/error.gsp"
	]
	
	def author = "Sebastien Blanc"
	def authorEmail = "sblanc@e-id.nl"
	def title = "Google URL Shortener Service"
	def description = '''\\
This plugins provides a abstraction layer around the google URL shortener service, you can shorten, expand and retrieve analytics from URL following the grails conventions.
'''
	
	// URL to the plugin's documentation
	def documentation = "http://grails.org/plugin/google-url-shortener"
	
	def doWithWebDescriptor = { xml ->
		// TODO Implement additions to web.xml (optional), this event occurs before 
	}
	
	def doWithSpring = {
		// TODO Implement runtime spring config (optional)
	}
	
	def doWithDynamicMethods = { applicationContext ->
		ShortenerService service = applicationContext.getBean('shortenerService')
		String.metaClass.shorten = {->
			def urlResource = service.shorten(delegate)
			return urlResource.shortUrl
		}
		String.metaClass.expand = {->
			def urlResource = service.expand(delegate)
			return urlResource.longUrl
		}
	}
	
	def doWithApplicationContext = { applicationContext ->
		// TODO Implement post initialization spring config (optional)
	}
	
	def onChange = { event ->
		// TODO Implement code that is executed when any artefact that this plugin is
		// watching is modified and reloaded. The event contains: event.source,
		// event.application, event.manager, event.ctx, and event.plugin.
	}
	
	def onConfigChange = { event ->
		// TODO Implement code that is executed when the project configuration changes.
		// The event is the same as for 'onChange'.
	}
}
