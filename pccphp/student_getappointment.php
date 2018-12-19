<?php
    
    $mysqli = new mysqli("localhost","root","","pcc");
    $sql = "SELECT * FROM get_appointments;";
    $response = mysqli_query($mysqli, $sql);
    $result = array();
    
    while($row = mysqli_fetch_array($response))
    {
        array_push($result, array("SEND_SID"=>$row['sid'], "SEND_DEAN"=>$row['dean'], "SEND_DATE"=>$row['date'], "SEND_TIME"=>$row['time']));
    }
    
    mysqli_close($mysqli);
    echo json_encode(array('server_response'=>$result));
    ?>
