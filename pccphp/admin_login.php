<?php
    if($_SERVER['REQUEST_METHOD'] == 'POST')
    {
        $username = $_POST['username'];
        $password = $_POST['password'];
        
        $mysqli = new mysqli("localhost","root","","pcc");
        $sql = "SELECT * FROM admins WHERE username='$username'";
        $response = mysqli_query($mysqli, $sql);
        
        $result = array();
        $result['login'] = array();
        
        if(mysqli_num_rows($response) === 1)
        {
            $row = mysqli_fetch_assoc($response);
            if(password_verify($password, $row['password']))
            {
                $index['id'] = $row['id'];
                $index['name'] = $row['name'];
                $index['username'] = $row['username'];
                array_push($result['login'], $index);
                
                $result['success'] = "1";
                $result['message'] = "success";
                echo json_encode($result);
                mysqli_close($mysqli);
            }
            else
            {
                $result['success'] = "0";
                $result['message'] = "error";
                echo json_encode($result);
                mysqli_close($mysqli);
            }
            
        }
        
    }
?>
