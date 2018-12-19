<?php

    $mysqli = new mysqli("localhost","root","","pcc");
    $sql = "SELECT * FROM faculties;";
    $response = mysqli_query($mysqli, $sql);
    $result = array();
    
    while($row = mysqli_fetch_array($response))
    {
        array_push($result, array("SEND_NAME"=>$row['name'], "SEND_TITLE"=>$row['title'], "SEND_PHONE"=>$row['phone'], "SEND_EMAIL"=>$row['email']));
    }
    
    mysqli_close($mysqli);
    echo json_encode(array('server_response'=>$result));
?>
