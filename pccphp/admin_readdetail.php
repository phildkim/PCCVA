<?php

    if($_SERVER['REQUEST_METHOD'] == 'POST')
    {
        $id = $_POST['id'];
        
        $mysqli = new mysqli("localhost","root","","pcc");
        
        $sql = "SELECT * FROM admins WHERE id='$id'";
        $response = mysqli_query($mysqli, $sql);
        
        $result = array();
        $result['read'] = array();
        
        if(mysqli_num_rows($response) === 1)
        {
            if($row = mysqli_fetch_assoc($response))
            {
                $h['id'] = $row['id'];
                $h['name'] = $row['name'];
                array_push($result["read"], $h);
                
                $result["success"] = "1";
                echo json_encode($result);
            }
        }
        else
        {
            $result['success'] = "0";
            $result['message'] = "Error!";
            echo json_encode($result);
            mysqli_close($mysqli);
        }
    }
?>
