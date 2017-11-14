<!-- <?php
// include('phpmailer.php');
// class Mail extends PhpMailer
// {
//     //Set default variables for all new objects
//     // public $From     = 'noreply@domain.com';
//     // public $FromName = SITETITLE;
//     // public $Host     = 'smtp.gmail.com';
//     // public $Mailer   = 'smtp';
//     // public $SMTPAuth = true;
//     // public $Username = 'vahedi.behzad@gmail.com';
//     // public $Password = 'Realeblis^(()';
//     // public $SMTPSecure = 'tls';
//     // public $WordWrap = 75;

//     public $mail = new PhpMailer; // create a new object
//     public $mail->IsSMTP(); // enable SMTP
//     public $mail->SMTPDebug = 1; // debugging: 1 = errors and messages, 2 = messages only
//     public $mail->SMTPAuth = true; // authentication enabled
//     public $mail->SMTPSecure = 'ssl'; // secure transfer enabled REQUIRED for Gmail
//     public $mail->Host = "smtp.gmail.com";
//     public $mail->Port = 465; // or 587
//     public $mail->IsHTML(true);
//     public $mail->Username = "vahedi.behzad@gmail.com";
//     public $mail->Password = "Realeblis^(()";
//     public $mail->SetFrom("vahedi.behazd@gmail.com");
//     // public $mail->Subject = "Test";
//     // public $mail->Body = "hello";
//     // public $mail->AddAddress("vahedi.behzad@gmail.com");

//  // if(!$mail->Send()) {
//  //    echo "Mailer Error: " . $mail->ErrorInfo;
//  // } else {
//  //    echo "Message has been sent";
//  // }
//     public function subject($subject)
//     {
//         $this->Subject = $subject;
//     }

//     public function body($body)
//     {
//         $this->Body = $body;
//     }

//     public function send()
//     {
//         $this->AltBody = strip_tags(stripslashes($this->Body))."\n\n";
//         $this->AltBody = str_replace("&nbsp;", "\n\n", $this->AltBody);
//         return parent::send();
//     }

// require 'classes/phpmailer/PHPMailerAutoload.php';

// $mail = new phpmailer;

// $mail->isSMTP();                            // Set mailer to use SMTP
// $mail->Host = 'smtp.gmail.com';             // Specify main and backup SMTP servers
// $mail->SMTPAuth = true;                     // Enable SMTP authentication
// $mail->Username = 'vahedi.behzad@gmail.com';          // SMTP username
// $mail->Password = 'Realeblis^(()'; // SMTP password
// $mail->SMTPSecure = 'tls';                  // Enable TLS encryption, `ssl` also accepted
// $mail->Port = 587;                          // TCP port to connect to

// $mail->setFrom('info@example.com', 'CodexWorld');
// $mail->addReplyTo('info@example.com', 'CodexWorld');
// $mail->addAddress('john@gmail.com');   // Add a recipient
// $mail->addCC('cc@example.com');
// $mail->addBCC('bcc@example.com');

// $mail->isHTML(true);  // Set email format to HTML

// $bodyContent = '<h1>How to Send Email using PHP in Localhost by CodexWorld</h1>';
// $bodyContent .= '<p>This is the HTML email sent from localhost using PHP script by <b>CodexWorld</b></p>';

// $mail->Subject = 'Email from Localhost by CodexWorld';
// $mail->Body    = $bodyContent;

// if(!$mail->send()) {
//     echo 'Message could not be sent.';
//     echo 'Mailer Error: ' . $mail->ErrorInfo;
// } else {
//     echo 'Message has been sent';
// }

// }

?>
 -->

 <?php
include('phpmailer.php');
class Mail extends PhpMailer
{
    // Set default variables for all new objects
    public $From     = 'noreply@domain.com';
    public $FromName = SITETITLE;
    //public $Host     = 'smtp.gmail.com';
    //public $Mailer   = 'smtp';
    //public $SMTPAuth = true;
    //public $Username = 'email';
    //public $Password = 'password';
    //public $SMTPSecure = 'tls';
    public $WordWrap = 75;

    public function subject($subject)
    {
        $this->Subject = $subject;
    }

    public function body($body)
    {
        $this->Body = $body;
    }

    public function send()
    {
        $this->AltBody = strip_tags(stripslashes($this->Body))."\n\n";
        $this->AltBody = str_replace("&nbsp;", "\n\n", $this->AltBody);
        return parent::send();
    }
}
