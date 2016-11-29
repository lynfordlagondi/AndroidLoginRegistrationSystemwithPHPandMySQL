<?php
/**
 * Created by PhpStorm.
 * User: asus
 * Date: 11/27/2016
 * Time: 12:02 PM
 */

require_once'../class/Functions.php';

ini_set('display_errors',1);
require_once'../libs/Slim/Slim.php';
\Slim\Slim::registerAutoloader();
$app = new \Slim\Slim();

function echoResponse($status_code,$response){

    $app = \Slim\Slim::getInstance();

    $app->status($status_code);

    $app->contentType('application/json');
    echo json_encode($response);
}

/**
 * user authentication
 */
 
$app->post('/login', function() use($app){

    $response = array("error" => FALSE);

    $email = $app->request->post('email');
    $password = $app->request->post('password');

    $database = new Functions();

    $user_login = $database->Authentication($email,$password);

    if($user_login != false){

        $response["error"] = FALSE;
        $response["message"]="User authentication success.";
        echo json_encode($response);

    }else{

        $response["error"] = TRUE;
        $response["message"]="authentication failed.";
        echo json_encode($response);
    }


});


/**
 * user registration
 */
$app->post('/registration',function() use($app){

    $response = array("error" => FALSE);

    $fullname = $app->request->post('fullname');
    $email = $app->request->post('email');
    $password = $app->request->post('password');

    $database = new Functions();

    if($database->EmailDuplication($email)){

        $response["error"] = TRUE;
        $response["message"]="User email exists.";
        echo json_encode($response);
		
    }else{

        $user_registration = $database->UserRegistration($fullname,$email,$password);

        if($user_registration != true){

            $response["error"] = FALSE;
            $response["message"]="User registration success.";
            echo json_encode($response);

        }else{

            $response["error"] = TRUE;
            $response["message"]="registration failed.";
            echo json_encode($response);
        }
    }
});
$app->run();
