package db.mongodb;
// This line needs manual import.
import static com.mongodb.client.model.Filters.eq;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bson.Document;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import db.DBConnection;
import entity.Item;
import entity.Item.ItemBuilder;
import external.ExternalAPI;
import external.ExternalAPIFactory;

public class MongoDBConnection implements DBConnection {
	private MongoClient mongoClient;
	private MongoDatabase db;
	public MongoDBConnection () {
		// Connects to local mongodb server.
		mongoClient = new MongoClient();
		db = mongoClient.getDatabase(MongoDBUtil.DB_NAME);
	}
	@Override
	public void close() {
		if (mongoClient != null) {
			mongoClient.close();
		}
	}
	@Override
	public void setFavoriteItems(String userId, List<String> itemIds) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void unsetFavoriteItems(String userId, List<String> itemIds) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Set<String> getFavoriteItemIds(String userId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Set<Item> getFavoriteItems(String userId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Set<String> getCategories(String itemId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Item> searchItems(String userId, double lat, double lon, String term) {
		// Connect to external API
				ExternalAPI api = ExternalAPIFactory.getExternalAPI();
				List<Item> items = api.search(lat, lon, term);
				for (Item item : items) {
					// Save the item into our own db.
					saveItem(item);
				}
				return items;
	}
	@Override
	public void saveItem(Item item) {
		// You can construct the query like
		// db.getCollection("items").find(new Document().append("item_id", item.getItemId()))
		// But the java drive provides you a clearer way to do this.
		FindIterable<Document> iterable = db.getCollection("items").find(eq("item_id", item.getItemId()));
		if (iterable.first() == null) {
			db.getCollection("items")
					.insertOne(new Document().append("item_id", item.getItemId()).append("name", item.getName())
							.append("city", item.getCity()).append("state", item.getState())
							.append("country", item.getCountry()).append("zip_code", item.getZipcode())
							.append("rating", item.getRating()).append("address", item.getAddress())
							.append("latitude", item.getLatitude()).append("longitude", item.getLongitude())
							.append("description", item.getDescription()).append("snippet", item.getSnippet())
							.append("snippet_url", item.getSnippetUrl()).append("image_url", item.getImageUrl())
							.append("url", item.getUrl()).append("categories", item.getCategories()));
		}
		
	}
	@Override
	public String getFullname(String userId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean verifyLogin(String userId, String password) {
		// TODO Auto-generated method stub
		return false;
	}
}
