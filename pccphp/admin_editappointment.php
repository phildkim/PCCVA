<?php

    if($_SERVER['REQUEST_METHOD'] == 'POST')
    {
        $id = $_POST['id'];
        $date = $_POST['date'];
        $times = $_POST['times'];
        
        $mysqli = new mysqli("localhost","root","","pcc");
        $sql = "UPDATE set_appointments SET times='$times' WHERE id='$id' AND date='$date'";
        
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
