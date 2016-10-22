using Microsoft.WindowsAzure;
using Microsoft.WindowsAzure.Storage;
using Microsoft.WindowsAzure.Storage.Blob;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;

namespace transporterService.Utils
{
    public class BlobUtils
    {
        private CloudBlobClient blobClient;

        public BlobUtils()
        {
            string storageAccountName = "dover";
            string storageAccountKey = "";
            
            string connectionString = string.Format(@"DefaultEndpointsProtocol=https;AccountName={0};AccountKey={1}",
              storageAccountName, storageAccountKey);

            CloudStorageAccount cloudStorageAccount = CloudStorageAccount.Parse(connectionString);

            // Create the blob client.
            blobClient = cloudStorageAccount.CreateCloudBlobClient();
            // Retrieve a reference to a container.
            //CloudBlobContainer container = blobClient.GetContainerReference("mycontainer");
        }

        public string getBlobBase64(string blobUrl)
        {
            var url = new Uri(blobUrl);
            string firstseg = url.Segments[0];
            string containerName = url.Segments[1];
            containerName = containerName.Replace("/", "");
            string blobName = url.Segments[2];
            blobName = blobName.Replace("/", "");

            // Retrieve a reference to a container.
            CloudBlobContainer container = blobClient.GetContainerReference(containerName);

            CloudBlockBlob blobSource = container.GetBlockBlobReference(blobName);
            if (blobSource.Exists())
            {
                blobSource.FetchAttributes();
                long fileByteLength = blobSource.Properties.Length;
                Byte[] myByteArray = new Byte[fileByteLength];
                blobSource.DownloadToByteArray(myByteArray, 0);

                // Convert the array to a base 64 sring.
                String s = Convert.ToBase64String(myByteArray);
                return s;
            }

            return "";
        }

        public MemoryStream getBlobStream(string blobUrl)
        {
            var url = new Uri(blobUrl);
            string firstseg = url.Segments[0];
            string containerName = url.Segments[1];
            containerName = containerName.Replace("/", "");
            string blobName = url.Segments[2];
            blobName = blobName.Replace("/", "");

            // Retrieve a reference to a container.
            CloudBlobContainer container = blobClient.GetContainerReference(containerName);

            CloudBlockBlob blobSource = container.GetBlockBlobReference(blobName);
            if (blobSource.Exists())
            {
                var memoryStream = new MemoryStream();
                blobSource.DownloadToStream(memoryStream);
                memoryStream.Seek(0, SeekOrigin.Begin);
                //memoryStream.Close();
                return memoryStream;
            }

            return null;
        }


    }
}