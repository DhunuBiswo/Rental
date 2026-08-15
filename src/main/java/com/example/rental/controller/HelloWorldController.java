package com.example.rental.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/hello")
    public String hello() {
        return """
                <html>
                <head>
                    <style>
                        body {
                            margin: 0;
                            height: 100vh;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            background: linear-gradient(135deg, #ff9a9e, #fad0c4);
                            font-family: Arial, sans-serif;
                            text-align: center;
                        }

                        .container {
                            background: white;
                            padding: 60px;
                            border-radius: 25px;
                            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
                        }

                        h1 {
                            font-size: 70px;
                            color: #e91e63;
                            margin-bottom: 20px;
                        }

                        p {
                            font-size: 30px;
                            color: #555;
                        }

                        .heart {
                            font-size: 80px;
                            animation: heartbeat 1s infinite;
                        }

                        @keyframes heartbeat {
                            0% { transform: scale(1); }
                            50% { transform: scale(1.2); }
                            100% { transform: scale(1); }
                        }
                    </style>
                </head>

                <body>
                    <div class="container">

                        <div class="heart">❤️</div>

                        <h1>I Love You Tiyan</h1>

                        <p>You are the most beautiful part of my life.</p>

                        <p>Forever with you? 💕</p>

                    </div>
                </body>
                </html>
                """;
    }
}