<?php  
error_reporting(E_ALL);
ini_set("disply_errors",1);

$target_dir = "./upload/";  
$target_file_name = $target_dir.basename($_FILES["file"]["name"]);  
$response = array();  
  
// Check if image file is an actual image or fake image  
if (isset($_FILES["file"]))   
{  
   if (move_uploaded_file($_FILES["file"]["tmp_name"], $target_file_name))   
   {  
     $success = true;  
     $message = "Successfully Uploaded";  
   }  
   else   
   {  
      $success = false;  
      $message = "Error while uploading";  
   }  
}  
else   
{  
      $success = false;  
      $message = "Required Field Missing";  
}  

$filebody = $_FILES;

$response["success"] = $success;  
$response["message"] = $message; 
$response["file_body"] = $filebody; 
// $response["file_body2"] = $filebody2; 

$response["tmpname"] = $_FILES["file"]["tmp_name"];
$response["targetfilename"] = $target_file_name;

echo json_encode($response);  
  
?>  
