package com.example.fugliara.ccs002.server.blob;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class AzureBlob extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "TAG_AzureBlob";
    private final String storageConnectionString =
            "DefaultEndpointsProtocol=https;" +
                    "AccountName=dover;" +
                    "AccountKey=i1TOvjdLlC72zkORL7Y6hbYdKw+h407oXGVhm/OoHEaTSMQDjozRAEV5zE4WD/JKSCQsGTL1CtA0zxZuFhtZxg==";
    private String action;
    private String containerName;
    private String file;
    private AzureBlobResult result;

    public AzureBlob(final String action, final String containerName, final String file, AzureBlobResult result) {
        this.action = action;
        this.containerName = containerName;
        this.file = file;
        this.result = result;
    }

    private void createContainer(final String containerName) {
        try {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            // Get a reference to a container.
            // The container name must be lower case
            CloudBlobContainer container = blobClient.getContainerReference(containerName);

            // Create the container if it does not exist.
            container.createIfNotExists();

            result.success();
        } catch (Exception e) {
            // Output the stack trace.
            Log.d(TAG, e.getMessage());
            result.fail();
        }
    }

    private void upload(final String containerName, final String filePath) {
        try {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            // Retrieve reference to a previously created container.
            CloudBlobContainer container = blobClient.getContainerReference(containerName);

            // Create or overwrite the "myimage.jpg" blob with contents from a local file.
            CloudBlockBlob blob = container.getBlockBlobReference(filePath.substring(filePath.lastIndexOf("/") + 1));
            File source = new File(filePath);
            blob.upload(new FileInputStream(source), source.length());
            result.success();
        } catch (Exception e) {
            // Output the stack trace.
            Log.d(TAG, e.getMessage());
            result.fail();
        }
    }

    private void list(final String containerName) {
        try {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            // Retrieve reference to a previously created container.
            CloudBlobContainer container = blobClient.getContainerReference(containerName);

            // Loop over blobs within the container and output the URI to each of them.
            for (ListBlobItem blobItem : container.listBlobs()) {
                Log.d(TAG, blobItem.getUri().toString());
            }
            result.success();

        } catch (Exception e) {
            // Output the stack trace.
            Log.d(TAG, e.getMessage());
            result.fail();
        }
    }

    private void download(final String containerName) {
        try {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            // Retrieve reference to a previously created container.
            CloudBlobContainer container = blobClient.getContainerReference(containerName);

            // Loop through each blob item in the container.
            for (ListBlobItem blobItem : container.listBlobs()) {
                // If the item is a blob, not a virtual directory.
                if (blobItem instanceof CloudBlob) {
                    // Download the item and save it to a file with the same name.
                    CloudBlob blob = (CloudBlob) blobItem;
                    blob.download(new FileOutputStream(blob.getName()));
                }
            }
            result.success();
        } catch (Exception e) {
            // Output the stack trace.
            Log.d(TAG, e.getMessage());
            result.fail();
        }
    }

    private void delete(final String containerName, final String fileName) {
        try {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            // Retrieve reference to a previously created container.
            CloudBlobContainer container = blobClient.getContainerReference(containerName);

            // Retrieve reference to a blob named "myimage.jpg".
            CloudBlockBlob blob = container.getBlockBlobReference(fileName);

            // Delete the blob.
            blob.deleteIfExists();
            result.success();
        } catch (Exception e) {
            // Output the stack trace.
            Log.d(TAG, e.getMessage());
            result.fail();
        }
    }

    private void deleteContainer(final String containerName) {
        try {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            // Retrieve reference to a previously created container.
            CloudBlobContainer container = blobClient.getContainerReference(containerName);

            // Delete the blob container.
            container.deleteIfExists();
            result.success();
        } catch (Exception e) {
            // Output the stack trace.
            Log.d(TAG, e.getMessage());
            result.fail();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        switch (action) {
            case "createContainer":
                createContainer(containerName);
                break;
            case "upload":
                upload(containerName, file);
                break;
            case "list":
                list(containerName);
                break;
            case "download":
                download(containerName);
                break;
            case "delete":
                delete(containerName, file);
                break;
            case "deleteContainer":
                deleteContainer(containerName);
                break;
        }

        return null;
    }
}
