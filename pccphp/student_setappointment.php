<?php

    if($_SERVER['REQUEST_METHOD'] == 'POST')
    {
        $name = $_POST['name'];
        $dean = $_POST['dean'];
        $date = $_POST['date'];
        $time = $_POST['time'];
        
        $mysqli = new mysqli("localhost","root","","pcc");
        $sql = "INSERT INTO get_appointments (name, dean, date, time) VALUES ('$name', '$dean', '$date', '$time')";
        
        if(mysqli_query($mysqli, $sql))
        {
            $result["success"] = "1";
            $result["message"] = "success";
            echo json_encode($result);
            mysqli_close($mysqli);
        }
        else
        {
            $result["success"] = "0";
            $result["message"] = "error";
            echo json_encode($result);
            mysqli_close($mysqli);
        }
    }
?>
