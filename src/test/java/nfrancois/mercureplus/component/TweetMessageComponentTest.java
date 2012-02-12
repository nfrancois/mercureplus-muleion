package nfrancois.mercureplus.component;

import junit.framework.Assert;
import nfrancois.mercureplus.model.mercureplus.Message;

import org.junit.Test;

public class TweetMessageComponentTest {
	
	
	public TweetMessageComponent component = new TweetMessageComponent();
	
	@Test
	public void shouldnt_be_change() throws Exception{
		// Given
		String content = "message court de moins de 140 caractères";
		Message message = new Message(content, "http://goo.gl/test", false);
		
		// When
		String twitMessage = component.twitMessage(message);
		
		// Then
		Assert.assertNotNull(twitMessage);
		Assert.assertTrue(twitMessage.length() < 140);
		Assert.assertEquals(content, twitMessage);
	}
	
	@Test
	public void should_just_add_a_short_link_when_rich_content() throws Exception{
		// Given
		String content = "message court de moins de 140 caractères";
		Message message = new Message(content, "http://goo.gl/test", true);
		
		// When
		String twitMessage = component.twitMessage(message);		
		
		// Then
		Assert.assertNotNull(twitMessage);
		Assert.assertTrue(twitMessage.length() < 140);
		Assert.assertEquals("message court de moins de 140 caractères http://goo.gl/test", twitMessage);
	}
	
	@Test
	public void should_trunk_content() throws Exception{
		// Given
		String content = "message de test qui contient suffisament de caractères pour le tronquer avec quelques points de suspension, par contre, il n'y a pas de contenu riche";
		Message message = new Message(content, "http://goo.gl/test", false);
		
		// When
		String twitMessage = component.twitMessage(message);		
		
		// Then
		Assert.assertNotNull(twitMessage);
		Assert.assertEquals(140, twitMessage.length());
		Assert.assertEquals("message de test qui contient suffisament de caractères pour le tronquer avec quelques points de suspension, par contre... http://goo.gl/test", twitMessage);
	}	
	
	@Test
	public void should_trunk_content_with_rich_content() throws Exception{
		// Given
		String content = "message de test qui contient suffisament de caractères pour le tronquer avec quelques points de suspension, par contre, mais avec du contenu riche";
		Message message = new Message(content, "http://goo.gl/test", true);
		
		// When
		String twitMessage = component.twitMessage(message);		
		
		// Then
		Assert.assertNotNull(twitMessage);
		Assert.assertEquals(140, twitMessage.length());
		Assert.assertEquals("message de test qui contient suffisament de caractères pour le tronquer avec quelques points de suspension, par contre... http://goo.gl/test", twitMessage);
	}
	

}
