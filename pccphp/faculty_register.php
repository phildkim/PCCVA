<?php

    if($_SERVER['REQUEST_METHOD'] == 'POST')
    {
        $name = $_POST['name'];
        $title = $_POST['title'];
        $phone = $_POST['phone'];
        $email = $_POST['email'];
        
        $mysqli = new mysqli("localhost","root","","pcc");
        $sql = "INSERT INTO faculties (name, title, phone, email) VALUES ('$name', '$title', '$phone', '$email')";
        
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
