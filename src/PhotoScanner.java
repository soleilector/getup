import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.FaceAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.cloud.vision.v1.LocalizedObjectAnnotation;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;

public class PhotoScanner {

	public static ArrayList<String> scanPhoto() {
		Scanner scan = new Scanner(System.in);

		String filePath = "";// = "C:\\Users\\demon\\Downloads\\Downloadsv2\\Personal\\Dorothyfish.webp";
		String apiKeyPath = "C:\\Users\\demon\\Downloads\\getup-398602-2bc7710d5f37.json";

		JFileChooser chooser = new JFileChooser();
		int status = chooser.showOpenDialog(null);
		if (status == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			if (file == null) {
				return null;
			}

			String fileName = chooser.getSelectedFile().getAbsolutePath();
			filePath = fileName;

			ArrayList<String> objects = detectObjects(apiKeyPath, filePath);
			
			
			System.out.printf("\nFound %d objects in file...\n",objects.size()); // lsit what objects were found 
			for (int x = 0; x < objects.size(); x++) { // for every object
				System.out.printf("\t%s", objects.get(x)); // 
			}
			
			
			return objects;

		}
		return null;
	}

	/**
	 * Detects localized objects in the specified local image.
	 *
	 * @param filePath The path to the file to perform localized object detection
	 *                 on.
	 * @throws Exception   on errors while closing the client.
	 * @throws IOException on Input/Output errors.
	 */
	public static ArrayList<String> detectObjects(String apiKeyFilePath, String detectFilePath) {
		try {
			List<AnnotateImageRequest> requests = new ArrayList<>();
			ByteString imgBytes = ByteString.readFrom(new FileInputStream(detectFilePath));
			com.google.cloud.vision.v1.Image img = com.google.cloud.vision.v1.Image.newBuilder().setContent(imgBytes)
					.build();
			Feature landMarkFeature = Feature.newBuilder().setType(Feature.Type.LANDMARK_DETECTION).build();
			Feature objectFeature = Feature.newBuilder().setType(Type.OBJECT_LOCALIZATION).build();
			Feature facesFeature = Feature.newBuilder().setType(Feature.Type.FACE_DETECTION).build();
			AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
					.addFeatures(objectFeature)
					.addFeatures(landMarkFeature)
					.addFeatures(facesFeature)
					.setImage(img).build();
			requests.add(request);

			// Initialize client that will be used to send requests. This client only needs
			// to be created
			// once, and can be reused for multiple requests. After completing all of your
			// requests, call
			// the "close" method on the client to safely clean up any remaining background
			// resources.

			CredentialsProvider credentialsProvider = FixedCredentialsProvider
					.create(ServiceAccountCredentials.fromStream(new FileInputStream(apiKeyFilePath)));

			ImageAnnotatorSettings imageAnnotatorSettings = ImageAnnotatorSettings.newBuilder()
					.setCredentialsProvider(credentialsProvider).build();

			ImageAnnotatorClient client = ImageAnnotatorClient.create(imageAnnotatorSettings);
			// Perform the request
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			// Display the results
			// String[] detectedObjects = {};
			ArrayList<String> detectedObjects = new ArrayList<>();
			for (AnnotateImageResponse res : responses) {
				for (LocalizedObjectAnnotation entity : res.getLocalizedObjectAnnotationsList()) {
					String thisObj = entity.getName();
					boolean isDupe = false;
					
					for (String foundObj : detectedObjects) { if (foundObj.equals(thisObj)) { isDupe = true; } } // ensure there are no duplicates
					
					if (!isDupe) {
						detectedObjects.add(thisObj);
						System.out.format("+++++++++++===========++++ Found object: %s\n", thisObj);
					}
				}
				
				for (EntityAnnotation annotation : res.getLandmarkAnnotationsList()) {
					//System.out.println("EEK");
					String thisLandmark = annotation.getDescription();
					boolean isDupe = false;
					
					for (String foundLandmark : detectedObjects) { if (foundLandmark.equals(thisLandmark)) { isDupe = true; } } // ensure there are no duplicates
					
					if (!isDupe) {
						detectedObjects.add(thisLandmark);
						System.out.format("+++++++++++===========++++ Found object: %s\n", thisLandmark);
					}
				}
				
				for (FaceAnnotation annotation : res.getFaceAnnotationsList()) {
					boolean isJoy = annotation.getJoyLikelihoodValue() >= 4;
					boolean isAnger = annotation.getAngerLikelihoodValue() >= 4;
					boolean isSad = annotation.getSorrowLikelihoodValue() >= 4;
					boolean isSurprise = annotation.getSurpriseLikelihoodValue() >= 4;
					
					HashMap<String, Boolean> emotionValues = new HashMap<>() {{
						put("Joy", isJoy);
						put("Anger", isAnger);
						put("Sorrow", isSad);
						put("Surprise", isSurprise);
					}}; 
					
					System.out.printf("moods:\n\tJoy Value: %d\n\tAnger Value: %d\n\tSad Value: \n\tSurprise Value: %d\n",
							annotation.getJoyLikelihoodValue(),
							annotation.getAngerLikelihoodValue(),
							annotation.getSorrowLikelihoodValue(),
							annotation.getSurpriseLikelihoodValue()
							);
					
					for (String emotion : emotionValues.keySet()) {
						boolean isDupe = false;
						for (String foundEmotion : detectedObjects) {
							if (foundEmotion.equals(emotion)) {
								isDupe = true;
								break;
							} 
						} // ensure there are no duplicates
						if (!isDupe && emotionValues.get(emotion) == true) {
							detectedObjects.add(emotion);
							System.out.format("+++++++++++===========++++ Found emotion: %s\n", emotion);
						}
						
					}
				}
			}
			

			//System.out.println(detectedObjects.size());
			
			return detectedObjects;

		} catch (Exception e) {
			return null;
			// e.printStackTrace();
		}
	}

	/*
	 * WORKING public static void detectLocalizedObjects(String apiKeyPath,String
	 * filePath) {
	 * 
	 * try { List<AnnotateImageRequest> requests = new ArrayList<>(); ByteString
	 * imgBytes = ByteString.readFrom(new FileInputStream(filePath));
	 * com.google.cloud.vision.v1.Image img =
	 * com.google.cloud.vision.v1.Image.newBuilder().setContent(imgBytes).build();
	 * AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
	 * .addFeatures(Feature.newBuilder().setType(Type.OBJECT_LOCALIZATION))
	 * .setImage(img) .build(); requests.add(request);
	 * 
	 * // Initialize client that will be used to send requests. This client only
	 * needs to be created // once, and can be reused for multiple requests. After
	 * completing all of your requests, call // the "close" method on the client to
	 * safely clean up any remaining background resources.
	 * 
	 * CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(
	 * ServiceAccountCredentials.fromStream(new FileInputStream(apiKeyPath)));
	 * 
	 * ImageAnnotatorSettings imageAnnotatorSettings =
	 * ImageAnnotatorSettings.newBuilder()
	 * .setCredentialsProvider(credentialsProvider) .build();
	 * 
	 * ImageAnnotatorClient client =
	 * ImageAnnotatorClient.create(imageAnnotatorSettings); // Perform the request
	 * BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
	 * List<AnnotateImageResponse> responses = response.getResponsesList();
	 * 
	 * // Display the results for (AnnotateImageResponse res : responses) { for
	 * (LocalizedObjectAnnotation entity : res.getLocalizedObjectAnnotationsList())
	 * { System.out.format("Object name: %s%n", entity.getName());
	 * System.out.format("Confidence: %s%n", entity.getScore());
	 * System.out.format("Normalized Vertices:%n"); entity .getBoundingPoly()
	 * .getNormalizedVerticesList() .forEach(vertex ->
	 * System.out.format("- (%s, %s)%n", vertex.getX(), vertex.getY())); } }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } }
	 */
	
}
