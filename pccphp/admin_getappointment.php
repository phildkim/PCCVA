<?php

    $mysqli = new mysqli("localhost","root","","pcc");
    $sql = "SELECT * FROM set_appointments;";
    $response = mysqli_query($mysqli, $sql);
    $result = array();
    
    while($row = mysqli_fetch_array($response))
    {
        array_push($result, array("SEND_ID"=>$row['id'], "SEND_NAME"=>$row['name'], "SEND_DATE"=>$row['date'], "SEND_STIME"=>$row['stime'], "SEND_ETIME"=>$row['etime'], "SEND_ATIME"=>$row['times']));
    }
    
    mysqli_close($mysqli);
    echo json_encode(array('server_response'=>$result));
?>
