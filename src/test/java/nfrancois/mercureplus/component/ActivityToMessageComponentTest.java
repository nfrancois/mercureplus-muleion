package nfrancois.mercureplus.component;

import nfrancois.mercureplus.model.googl.GooGlObject;
import nfrancois.mercureplus.model.gplus.Activity;
import nfrancois.mercureplus.model.gplus.ActivityObject;
import nfrancois.mercureplus.model.gplus.ActivityObjectAttachments;
import nfrancois.mercureplus.model.mercureplus.Message;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class ActivityToMessageComponentTest {
	
	ActivityToMessageComponent component = new ActivityToMessageComponent();
	@Mock
	RestTemplate restTemplate;
	
	@Before
	public void setUp() {
		component.setRestTemplate(restTemplate);
	}
	
	
	@Test
	@SuppressWarnings("unchecked")
	public void simple_message() throws Exception{
		// Given
		Activity activity = new Activity();
		ActivityObject activityObject = new ActivityObject();
		activityObject.setContent("Un petit message court");
		activity.setObject(activityObject);
		activity.setUrl("http://uneurlpluslongue");
		ResponseEntity<GooGlObject> gooGlEntityResponse = Mockito.mock(ResponseEntity.class);
		GooGlObject gooGlResponse = Mockito.mock(GooGlObject.class);
		Mockito.when(gooGlEntityResponse.getBody()).thenReturn(gooGlResponse);
		Mockito.when(gooGlResponse.getShortUrl()).thenReturn("http://goo.gl/test");
		Mockito.when(restTemplate.postForEntity(Matchers.argThat(CoreMatchers.equalTo(ActivityToMessageComponent.GOO_GL_URL_REQUEST)), //
											    Matchers.argThat(CoreMatchers.equalTo(new GooGlObject("http://uneurlpluslongue"))), //
											    Matchers.argThat(CoreMatchers.equalTo(GooGlObject.class)))).thenReturn(gooGlEntityResponse);
		// When
		Message message = component.transform(activity);
		
		// Then
		Assert.assertNotNull(message);
		Assert.assertEquals("Un petit message court",message.getContent());
		Assert.assertEquals("http://goo.gl/test", message.getShortUrl());
		Assert.assertFalse(message.isHasRichContent());
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void simple_message_with_url() throws Exception{
		// Given
		Activity activity = new Activity();
		ActivityObject activityObject = new ActivityObject();
		activityObject.setContent("Un petit message court http://superblog.com");
		activity.setObject(activityObject);
		activity.setUrl("http://uneurlpluslongue");	
		ResponseEntity<GooGlObject> gooGlEntityResponse = Mockito.mock(ResponseEntity.class);
		GooGlObject gooGlResponse = Mockito.mock(GooGlObject.class);
		Mockito.when(gooGlEntityResponse.getBody()).thenReturn(gooGlResponse);
		Mockito.when(gooGlResponse.getShortUrl()).thenReturn("http://goo.gl/test");
		Mockito.when(restTemplate.postForEntity(Matchers.argThat(CoreMatchers.equalTo(ActivityToMessageComponent.GOO_GL_URL_REQUEST)), //
											    Matchers.argThat(CoreMatchers.equalTo(new GooGlObject("http://uneurlpluslongue"))), //
											    Matchers.argThat(CoreMatchers.equalTo(GooGlObject.class)))).thenReturn(gooGlEntityResponse);
		
		ResponseEntity<GooGlObject> gooGlEntityResponse2 = Mockito.mock(ResponseEntity.class);
		GooGlObject gooGlResponse2 = Mockito.mock(GooGlObject.class);
		Mockito.when(gooGlEntityResponse2.getBody()).thenReturn(gooGlResponse2);
		Mockito.when(gooGlResponse2.getShortUrl()).thenReturn("http://goo.gl/test2");
		Mockito.when(restTemplate.postForEntity(Matchers.argThat(CoreMatchers.equalTo(ActivityToMessageComponent.GOO_GL_URL_REQUEST)), //
											    Matchers.argThat(CoreMatchers.equalTo(new GooGlObject("http://superblog.com"))), //
											    Matchers.argThat(CoreMatchers.equalTo(GooGlObject.class)))).thenReturn(gooGlEntityResponse2);
		
		// When
		Message message = component.transform(activity);
		
		// Then
		Assert.assertNotNull(message);
		Assert.assertEquals("Un petit message court http://goo.gl/test2",message.getContent());
		Assert.assertEquals("http://goo.gl/test", message.getShortUrl());
		Assert.assertFalse(message.isHasRichContent());
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void simple_message_with_photo() throws Exception{
		// Given
		Activity activity = new Activity();
		ActivityObject activityObject = new ActivityObject();
		activityObject.setContent("Un petit message court");
		ActivityObjectAttachments activityObjectAttachments = new ActivityObjectAttachments();
		activityObjectAttachments.setObjectType("photo");
		activityObjectAttachments.setUrl("http://lienverslaphoto");
		activityObject.setAttachments(Lists.newArrayList(activityObjectAttachments));
		activity.setObject(activityObject);
		activity.setUrl("http://uneurlpluslongue");	
		ResponseEntity<GooGlObject> gooGlEntityResponse = Mockito.mock(ResponseEntity.class);
		GooGlObject gooGlResponse = Mockito.mock(GooGlObject.class);
		Mockito.when(gooGlEntityResponse.getBody()).thenReturn(gooGlResponse);
		Mockito.when(gooGlResponse.getShortUrl()).thenReturn("http://goo.gl/test");
		Mockito.when(restTemplate.postForEntity(Matchers.argThat(CoreMatchers.equalTo(ActivityToMessageComponent.GOO_GL_URL_REQUEST)), //
											    Matchers.argThat(CoreMatchers.equalTo(new GooGlObject("http://uneurlpluslongue"))), //
											    Matchers.argThat(CoreMatchers.equalTo(GooGlObject.class)))).thenReturn(gooGlEntityResponse);

		ResponseEntity<GooGlObject> gooGlEntityResponse2 = Mockito.mock(ResponseEntity.class);
		GooGlObject gooGlResponse2 = Mockito.mock(GooGlObject.class);
		Mockito.when(gooGlEntityResponse2.getBody()).thenReturn(gooGlResponse2);
		Mockito.when(gooGlResponse2.getShortUrl()).thenReturn("http://goo.gl/test2");
		Mockito.when(restTemplate.postForEntity(Matchers.argThat(CoreMatchers.equalTo(ActivityToMessageComponent.GOO_GL_URL_REQUEST)), //
											    Matchers.argThat(CoreMatchers.equalTo(new GooGlObject("http://lienverslaphoto"))), //
											    Matchers.argThat(CoreMatchers.equalTo(GooGlObject.class)))).thenReturn(gooGlEntityResponse2);		
		
		
		// When
		Message message = component.transform(activity);
		
		// Then
		Assert.assertNotNull(message);
		Assert.assertEquals("Un petit message court",message.getContent());
		Assert.assertEquals("http://goo.gl/test", message.getShortUrl());
		Assert.assertTrue(message.isHasRichContent());		
		
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void simple_message_with_video() throws Exception{
		// Given
		Activity activity = new Activity();
		ActivityObject activityObject = new ActivityObject();
		activityObject.setContent("Un petit message court");
		ActivityObjectAttachments activityObjectAttachments = new ActivityObjectAttachments();
		activityObjectAttachments.setObjectType("video");
		activityObjectAttachments.setUrl("http://lienverslavideo");
		activityObject.setAttachments(Lists.newArrayList(activityObjectAttachments));
		activity.setObject(activityObject);
		activity.setUrl("http://uneurlpluslongue");	
		ResponseEntity<GooGlObject> gooGlEntityResponse = Mockito.mock(ResponseEntity.class);
		GooGlObject gooGlResponse = Mockito.mock(GooGlObject.class);
		Mockito.when(gooGlEntityResponse.getBody()).thenReturn(gooGlResponse);
		Mockito.when(gooGlResponse.getShortUrl()).thenReturn("http://goo.gl/test");
		Mockito.when(restTemplate.postForEntity(Matchers.argThat(CoreMatchers.equalTo(ActivityToMessageComponent.GOO_GL_URL_REQUEST)), //
											    Matchers.argThat(CoreMatchers.equalTo(new GooGlObject("http://uneurlpluslongue"))), //
											    Matchers.argThat(CoreMatchers.equalTo(GooGlObject.class)))).thenReturn(gooGlEntityResponse);

		ResponseEntity<GooGlObject> gooGlEntityResponse2 = Mockito.mock(ResponseEntity.class);
		GooGlObject gooGlResponse2 = Mockito.mock(GooGlObject.class);
		Mockito.when(gooGlEntityResponse2.getBody()).thenReturn(gooGlResponse2);
		Mockito.when(gooGlResponse2.getShortUrl()).thenReturn("http://goo.gl/test2");
		Mockito.when(restTemplate.postForEntity(Matchers.argThat(CoreMatchers.equalTo(ActivityToMessageComponent.GOO_GL_URL_REQUEST)), //
											    Matchers.argThat(CoreMatchers.equalTo(new GooGlObject("http://lienverslavideo"))), //
											    Matchers.argThat(CoreMatchers.equalTo(GooGlObject.class)))).thenReturn(gooGlEntityResponse2);		
		
		
		// When
		Message message = component.transform(activity);
		
		// Then
		Assert.assertNotNull(message);
		Assert.assertEquals("Un petit message court",message.getContent());
		Assert.assertEquals("http://goo.gl/test", message.getShortUrl());
		Assert.assertTrue(message.isHasRichContent());		
		
	}	
	
	@Test
	@SuppressWarnings("unchecked")
	public void simple_message_with_album() throws Exception{
		// Given
		Activity activity = new Activity();
		ActivityObject activityObject = new ActivityObject();
		activityObject.setContent("Un petit message court");
		ActivityObjectAttachments activityObjectAttachments = new ActivityObjectAttachments();
		activityObjectAttachments.setObjectType("photo-album");
		activityObjectAttachments.setUrl("http://lienversphotoalbum");
		activityObject.setAttachments(Lists.newArrayList(activityObjectAttachments));
		activity.setObject(activityObject);
		activity.setUrl("http://uneurlpluslongue");	
		ResponseEntity<GooGlObject> gooGlEntityResponse = Mockito.mock(ResponseEntity.class);
		GooGlObject gooGlResponse = Mockito.mock(GooGlObject.class);
		Mockito.when(gooGlEntityResponse.getBody()).thenReturn(gooGlResponse);
		Mockito.when(gooGlResponse.getShortUrl()).thenReturn("http://goo.gl/test");
		Mockito.when(restTemplate.postForEntity(Matchers.argThat(CoreMatchers.equalTo(ActivityToMessageComponent.GOO_GL_URL_REQUEST)), //
											    Matchers.argThat(CoreMatchers.equalTo(new GooGlObject("http://uneurlpluslongue"))), //
											    Matchers.argThat(CoreMatchers.equalTo(GooGlObject.class)))).thenReturn(gooGlEntityResponse);

		ResponseEntity<GooGlObject> gooGlEntityResponse2 = Mockito.mock(ResponseEntity.class);
		GooGlObject gooGlResponse2 = Mockito.mock(GooGlObject.class);
		Mockito.when(gooGlEntityResponse2.getBody()).thenReturn(gooGlResponse2);
		Mockito.when(gooGlResponse2.getShortUrl()).thenReturn("http://goo.gl/test2");
		Mockito.when(restTemplate.postForEntity(Matchers.argThat(CoreMatchers.equalTo(ActivityToMessageComponent.GOO_GL_URL_REQUEST)), //
											    Matchers.argThat(CoreMatchers.equalTo(new GooGlObject("http://lienverslaphotoalbum"))), //
											    Matchers.argThat(CoreMatchers.equalTo(GooGlObject.class)))).thenReturn(gooGlEntityResponse2);		
		
		
		// When
		Message message = component.transform(activity);
		
		// Then
		Assert.assertNotNull(message);
		Assert.assertEquals("Un petit message court",message.getContent());
		Assert.assertEquals("http://goo.gl/test", message.getShortUrl());
		Assert.assertTrue(message.isHasRichContent());		
		
	}	

	@Test
	@SuppressWarnings("unchecked")	
	public void reshare_a_share_without_comment() throws Exception{
		// Given
		Activity activity = new Activity();
		ActivityObject activityObject = new ActivityObject();
		ActivityObjectAttachments activityObjectAttachments = new ActivityObjectAttachments();
		activityObjectAttachments.setObjectType("article");
		activityObjectAttachments.setUrl("http://superarticle");
		activityObjectAttachments.setContent("Dans cet article nous allons parler de ...");
		activityObject.setAttachments(Lists.newArrayList(activityObjectAttachments));
		activity.setObject(activityObject);
		activity.setUrl("http://uneurlpluslongue");	
		ResponseEntity<GooGlObject> gooGlEntityResponse = Mockito.mock(ResponseEntity.class);
		GooGlObject gooGlResponse = Mockito.mock(GooGlObject.class);
		Mockito.when(gooGlEntityResponse.getBody()).thenReturn(gooGlResponse);
		Mockito.when(gooGlResponse.getShortUrl()).thenReturn("http://goo.gl/test");
		Mockito.when(restTemplate.postForEntity(Matchers.argThat(CoreMatchers.equalTo(ActivityToMessageComponent.GOO_GL_URL_REQUEST)), //
											    Matchers.argThat(CoreMatchers.equalTo(new GooGlObject("http://uneurlpluslongue"))), //
											    Matchers.argThat(CoreMatchers.equalTo(GooGlObject.class)))).thenReturn(gooGlEntityResponse);

		ResponseEntity<GooGlObject> gooGlEntityResponse2 = Mockito.mock(ResponseEntity.class);
		GooGlObject gooGlResponse2 = Mockito.mock(GooGlObject.class);
		Mockito.when(gooGlEntityResponse2.getBody()).thenReturn(gooGlResponse2);
		Mockito.when(gooGlResponse2.getShortUrl()).thenReturn("http://goo.gl/test2");
		Mockito.when(restTemplate.postForEntity(Matchers.argThat(CoreMatchers.equalTo(ActivityToMessageComponent.GOO_GL_URL_REQUEST)), //
											    Matchers.argThat(CoreMatchers.equalTo(new GooGlObject("http://lienverslaphotoalbum"))), //
											    Matchers.argThat(CoreMatchers.equalTo(GooGlObject.class)))).thenReturn(gooGlEntityResponse2);
		
		// When
		Message message = component.transform(activity);
		
		// Then
		Assert.assertNotNull(message);
		Assert.assertEquals("Dans cet article nous allons parler de ...",message.getContent());
		Assert.assertEquals("http://goo.gl/test", message.getShortUrl());
		Assert.assertFalse(message.isHasRichContent());
		
	}
	
}
