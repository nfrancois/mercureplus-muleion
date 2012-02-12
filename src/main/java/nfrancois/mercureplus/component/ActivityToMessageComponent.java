package nfrancois.mercureplus.component;

import java.util.Map;
import java.util.Set;

import nfrancois.mercureplus.model.googl.GooGlObject;
import nfrancois.mercureplus.model.gplus.Activity;
import nfrancois.mercureplus.model.gplus.ActivityObject;
import nfrancois.mercureplus.model.mercureplus.Message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


public class ActivityToMessageComponent {
	
	private static final Log LOG = LogFactory.getLog(ActivityToMessageComponent.class);

	public static final String GOO_GL_URL_REQUEST = "https://www.googleapis.com/urlshortener/v1/url";
	private static final String ARTICLE_ATTACHEMENT_TYPE = "article";
	private static final String PHOTO_ATTACHEMENT_TYPE = "photo";
	private static final String ALBUM_ATTACHEMENT_TYPE = "photo-album";
	private static final String VIDEO_ATTACHEMENT_TYPE = "video";
	private static final Set<String > RICH_CONTENT = Sets.newHashSet(ALBUM_ATTACHEMENT_TYPE, PHOTO_ATTACHEMENT_TYPE, VIDEO_ATTACHEMENT_TYPE);
	
	private RestTemplate restTemplate;
	
	public Message transform(Activity activity) throws Exception {
		String longUrl = activity.getUrl();
		ActivityObject activityObject = activity.getObject();
		// TODO voir si pbl google reader => le content n'a pas l'url de l'article
		String content = searchAndShortenizeUrl(cleanHtmlSymbols(getOriginContent(activityObject)));
		
		String shortUrl = shortenizeUrl(longUrl);
		if(LOG.isDebugEnabled()){
			LOG.debug("shortenize url="+longUrl+ " to "+shortUrl );
		}
		return new Message(content,shortUrl, hasRichContent(activityObject));
	}

	private String shortenizeUrl(String longUrl){
		ResponseEntity<GooGlObject> googlResponse = restTemplate.postForEntity(GOO_GL_URL_REQUEST, new GooGlObject(longUrl), GooGlObject.class);
		return googlResponse.getBody().getShortUrl();		
	}
	
	private String getOriginContent(ActivityObject activityObject) {
		String originContent = activityObject.getContent();
		if(Strings.isNullOrEmpty(originContent) && !CollectionUtils.isEmpty(activityObject.getAttachments()) && ARTICLE_ATTACHEMENT_TYPE.equals(activityObject.getAttachments().get(0).getObjectType())){
			// Cas de reshare d'un lien sans que personne n'est mit de commentaire
			originContent = activityObject.getAttachments().get(0).getContent();
		}
		return originContent;
	}
	
	private String cleanHtmlSymbols(String content){
		return Jsoup.parse(content).text();
	}
	
	private boolean hasRichContent(ActivityObject activityObject){
		if(!CollectionUtils.isEmpty(activityObject.getAttachments())){
			return RICH_CONTENT.contains(activityObject.getAttachments().get(0).getObjectType()); 
		}	
		return false;
	}
	
	private String searchAndShortenizeUrl(String content){
		Iterable<String> words = Splitter.on(' ').split(content);
		Iterable<String> urls = Iterables.filter(words, urlPredicate);
		Map<String, String> urlsMap = Maps.uniqueIndex(urls, shortUrlFunction);
		for(Map.Entry<String, String> entry : urlsMap.entrySet()){
			content = content.replaceAll(entry.getValue(), entry.getKey());
		}
		return content;
	}
	
	private Predicate<String> urlPredicate = new Predicate<String>(){

		@Override
		public boolean apply(String word) {
			return word.startsWith("http://") || word.startsWith("https://");
		}
		
	};
	
	private Function<String, String> shortUrlFunction = new Function<String, String>() {

		@Override
		public String apply(String longUrl) {
			return shortenizeUrl(longUrl);
		}
	};	
	
	
	
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

}
