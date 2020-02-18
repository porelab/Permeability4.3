package firebase;

import com.google.cloud.firestore.Blob;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.database.FirebaseDatabase;
//for connection with online cloud..

import static java.nio.charset.StandardCharsets.UTF_8;

public class FirebaseConnect {
	public static FirebaseDatabase db1;

	public static FirebaseConnect cc;
	public static Firestore db;

	public static FirestoreOptions options = null;
	public static FirebaseDatabase fb = null;

	public FirebaseConnect() {
		cc = this;
	}

	public static void InitApp(FirestoreOptions op, java.io.InputStream ii) {

		try {

			options = op;
			db = op.getService();

			// InputStream ii1=(InputStream)
			// cc.getClass().getResourceAsStream("/firebase/serviceAccountKey.json");

			FirebaseOptions options1 = new FirebaseOptions.Builder()
					.setCredential(FirebaseCredentials.fromCertificate(ii))
				
					.setDatabaseUrl("https://nyiapp-3a612.firebaseio.com").build();

			FirebaseApp app = FirebaseApp.initializeApp(options1);
			fb = FirebaseDatabase.getInstance(app);

			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
