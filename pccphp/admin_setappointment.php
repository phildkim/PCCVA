<?php

    if($_SERVER['REQUEST_METHOD'] == 'POST')
    {
        $id = $_POST['id'];
        $name = $_POST['name'];
        $date = $_POST['date'];
        $stime = $_POST['stime'];
        $etime = $_POST['etime'];
        $times = $_POST['times'];
        
        $mysqli = new mysqli("localhost","root","","pcc");
        $sql = "INSERT INTO set_appointments (id, name, date, stime, etime, times) VALUES ('$id', '$name', '$date', '$stime', '$etime', '$times')";
        
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
