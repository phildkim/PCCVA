<?php

    if($_SERVER['REQUEST_METHOD'] == 'POST')
    {
        $id = $_POST['id'];
        $name = $_POST['name'];
        $username = $_POST['username'];
        $email = $_POST['email'];
        $phone = $_POST['phone'];
        $major = $_POST['major'];
        
        $mysqli = new mysqli("localhost","root","","pcc");
        $sql = "UPDATE students SET name='$name', username='$username', email='$email', phone='$phone', major='$major' WHERE id='$id'";
        
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
