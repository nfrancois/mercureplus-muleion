package nfrancois.mercureplus.component;

import java.util.Collection;

import javax.annotation.Resource;

import nfrancois.mercureplus.model.gplus.Activity;
import nfrancois.mercureplus.model.gplus.ActivityFeed;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;


public class GPLusResponseComponent  {
	
	private static final Log LOG = LogFactory.getLog(GPLusResponseComponent.class);
	
	@Resource
	private MongoDBService mongoDBService;
	
	public Collection<Activity> getActivities(ActivityFeed activityFeed) {
			final DateTime lastSyncho = mongoDBService.getLastSyncho();
			LOG.info("Last synchro="+lastSyncho);
			Collection<Activity> newActivities = Collections2.filter(activityFeed.getItems(), new LastActivitiesFilter(lastSyncho));
			LOG.info(newActivities.size()+ " a twitter");
			if(!newActivities.isEmpty()){ // Mise a jour seulement si besoin
				boolean updateSyncho = mongoDBService.updateSyncho(activityFeed.getUpdated(), newActivities.size());
				if(!updateSyncho){ // Si fail l'aventure s'arrete
					newActivities.clear();
				}
			}
			return newActivities;
	}

	
	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}
	
	
	private static class LastActivitiesFilter implements Predicate<Activity>  {
		
		public DateTime lastSynchronisation;

		public LastActivitiesFilter(DateTime lastSynchronisation) {
			this.lastSynchronisation = lastSynchronisation;
		}

		@Override
		public boolean apply(Activity activity) {
			return lastSynchronisation.isBefore(activity.getUpdated());
		}
		
	}

	
}
