<?php

    if($_SERVER['REQUEST_METHOD'] == 'POST')
    {
        $id = $_POST['id'];
        $name = $_POST['name'];
        $username = $_POST['username'];
        $email = $_POST['email'];
        $phone = $_POST['phone'];
        $password = $_POST['password'];
        $major = $_POST['major'];
        
        $password = password_hash($password, PASSWORD_DEFAULT);
        $mysqli = new mysqli("localhost","root","","pcc");
        $sql = "INSERT INTO students (id, name, username, email, phone, password, major) VALUES ('$id', '$name', '$username', '$email', '$phone', '$password', '$major')";
        
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
