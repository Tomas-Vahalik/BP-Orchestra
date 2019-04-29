<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head>
        <link href="../loginStyle.css" rel="stylesheet" type="text/css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Secure Vaadin Application Demo Login</title>
        
        
    </head>
    <body>
        <div class = "header">            
            <div class="fit"><img src="../FIT-logo.jpg" style="height:100%"/></div>              
            <div class="notysek">NOTÝSEK - Aplikace pro distribuci not v orchestru</div>
            
            
        </div>
        
        <div class = "center">            
            <div class="login">         
        
		<form method="post" action="j_security_check" >
			<p>
				<span class="label">Username:</span> <input class="input" type="text" name="j_username"/>
			</p>
			<p>
				<span class="label">Password:</span> <input class="input" type="password" name="j_password"/>
			</p>
                    
			<p>
				<input class="button" type="submit" value="Login"/>
			</p>
                        <a class="bla" href="../register.jsp">Register</a>
                    
		</form>
               </div> 
               <div class = "obrazek">
                   <img src="../BP_Background.jpg" style=" position: absolute;
    top: -9999px;
    bottom: -9999px;
    left: -9999px;
    right: -9999px;
    margin: auto;
    transform:scale(0.48,0.48)"/>​
               </div>   
              
        </div>    
        <div class="footer">
            <p><img src="../UspinLogo.PNG" style="height:90%"/>​</p>
            <p class="desc">Supported by <a class="desc" href = "https://www.uspin.cz/cz/">Uspin</a></p>
        
         
        </div>    
        <!--><!-->
        
    </body>
</html>
