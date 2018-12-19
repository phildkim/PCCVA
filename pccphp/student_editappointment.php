<?php

    if($_SERVER['REQUEST_METHOD'] == 'POST')
    {
        $sid = $_POST['sid'];
        $time = $_POST['time'];
        
        $mysqli = new mysqli("localhost","root","","pcc");
        $sql = "DELETE FROM get_appointments WHERE sid='$sid' AND time='$time'";
        
        if(mysqli_query($mysqli, $sql)) {
            
            $result["success"] = "1";
            $result["message"] = "success";
            echo json_encode($result);
            mysqli_close($mysqli);
        }
    }
    else
    {
        $result["success"] = "0";
        $result["message"] = "error!";
        echo json_encode($result);
        mysqli_close($mysqli);
    }
?>
