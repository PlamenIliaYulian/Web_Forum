
#Users:
INSERT INTO users (user_email, password, first_name, last_name, username)
VALUES
    ('john.doe@example.com', 'AbCdEfGhIjKlMnOpQrSt', 'John', 'Doe', 'john_doe'),
    ('jane.smith@example.com', 'XyZaBcDeFgHiJkLmNoP', 'Jane', 'Smith', 'jane_smith'),
    ('michael.jones@example.com', 'PqRsTuVwXyZaBcDeFgH', 'Michael', 'Jones', 'michael_jones'),
    ('susan.miller@example.com', 'IjKlMnOpQrStUvWxYzA', 'Susan', 'Miller', 'susan_miller'),
    ('robert.white@example.com', 'HgFeDcBa0987654321', 'Robert', 'White', 'robert_white'),
    ('laura.wilson@example.com', 'LmNoPqRsTuVwXyZaBcD', 'Laura', 'Wilson', 'laura_wilson'),
    ('david.brown@example.com', 'MnOpQrStUvWxYzAbCdEf', 'David', 'Brown', 'david_brown'),
    ('emily.jackson@example.com', 'KlMnOpQrStUvWxYzAbC', 'Emily', 'Jackson', 'emily_jackson'),
    ('peter.anderson@example.com', 'XyZaBcDeFgHiJkLmNoPq', 'Peter', 'Anderson', 'peter_anderson'),
    ('natalie.hall@example.com', 'HgFeDcBa0987654321', 'Natalie', 'Hall', 'natalie_hall');
 
#Roles:
INSERT INTO roles (name)
VALUES
    ('ROLE_ADMIN'),
    ('ROLE_MEMBER'),
    ('ROLE_STAFF_MEMBER');
 
#Tags:
INSERT INTO tags (name) VALUES
                            ('JavaBasics'),
                            ('OOPJava'),
                            ('JavaCollections'),
                            ('JavaThreads'),
                            ('JavaExceptions'),
                            ('JavaIO'),
                            ('JavaNetworking'),
                            ('JavaSwing'),
                            ('JavaFX'),
                            ('SpringFramework'),
                            ('HibernateORM'),
                            ('JavaTesting'),
                            ('JavaWebDevelopment'),
                            ('JavaBestPractices');
 
 
#Posts:
INSERT INTO posts (title, content, user_id, created_on, likes, dislikes)
VALUES
    ('Understanding Java Multithreading', 'Multithreading is a crucial aspect of Java programming, allowing concurrent execution of tasks. It enhances performance by efficiently utilizing system resources.', 3, '2024-01-05 10:30:00', 0, 0),
    ('Exploring Java Spring Framework', 'The Spring Framework simplifies Java development by providing comprehensive infrastructure support. With features like dependency injection, it promotes modular and testable code.', 7, '2024-01-10 14:45:00', 0, 0),
    ('Java Design Patterns for Scalability', 'Applying design patterns is essential for building scalable Java applications. Patterns like Singleton and Observer contribute to creating robust and efficient systems.', 1, '2024-01-15 12:20:00', 0, 0),
    ('Effective Exception Handling in Java', 'Exception handling is a critical part of Java programming. By using try-catch blocks, developers can manage runtime errors and ensure graceful application behavior.', 8, '2024-01-03 16:00:00', 0, 0),
    ('Mastering Java Collections Framework', 'Java Collections offer a versatile set of data structures. From Lists to Maps, understanding and utilizing these classes is key for efficient data manipulation.', 6, '2024-01-18 08:10:00', 0, 0),
    ('Secure Coding Practices in Java', 'Security is paramount in Java development. Adhering to secure coding practices helps prevent vulnerabilities and ensures the integrity of your applications.', 2, '2024-01-08 09:50:00', 0, 0),
    ('JavaFX: Building Modern User Interfaces', 'JavaFX provides a platform for creating rich, interactive user interfaces. With features like FXML and CSS styling, developers can build modern and visually appealing applications.', 9, '2024-01-21 11:15:00', 0, 0),
    ('Java Networking: Socket Programming', 'Understanding socket programming is essential for building networked applications in Java. It enables communication between distributed systems through the use of sockets.', 4, '2024-01-02 13:30:00', 0, 0),
    ('Optimizing Java Code for Performance', 'Performance optimization is a continuous process in Java development. Techniques like code profiling and efficient algorithm implementation contribute to faster and more responsive applications.', 5, '2024-01-12 15:40:00', 0, 0),
    ('Unit Testing in Java with JUnit', 'JUnit is a powerful testing framework for Java. Adopting unit testing practices ensures the reliability and correctness of your code throughout the development process.', 10, '2024-01-23 17:00:00', 0, 0),
    ('Java Stream API: Functional Programming', 'The Stream API in Java facilitates functional programming paradigms. Leveraging streams allows developers to write concise and expressive code for data manipulation.', 3, '2024-01-06 10:45:00', 0, 0),
    ('Developing RESTful APIs with Java', 'Building RESTful APIs is a common requirement in Java development. Technologies like Spring Boot simplify the process, enabling developers to create scalable and robust APIs.', 7, '2024-01-13 14:30:00', 0, 0),
    ('Concurrency Challenges in Java Programming', 'Concurrency introduces challenges in Java development. Addressing issues such as race conditions and deadlock is crucial for building stable and reliable concurrent applications.', 1, '2024-01-17 16:20:00', 0, 0),
    ('Java 17 Features: What is New?', 'Java 17 introduces new features and enhancements. From pattern matching to sealed classes, staying updated on the latest language improvements is essential for modern Java development.', 6, '2024-01-20 08:45:00', 0, 0),
    ('Java Memory Management Best Practices', 'Effective memory management is vital for preventing memory leaks and optimizing Java applications. Understanding concepts like garbage collection contributes to efficient resource utilization.', 8, '2024-01-07 11:10:00', 0, 0),
    ('Building Microservices with Spring Boot', 'Microservices architecture is a popular approach in Java development. Using Spring Boot, developers can create modular and scalable microservices for building robust distributed systems.', 2, '2024-01-19 13:55:00', 0, 0),
    ('Java 3D Graphics Programming', 'Java supports 3D graphics programming through libraries like JOGL. Developing applications with 3D visualization enhances the user experience and opens up possibilities for innovative interfaces.', 9, '2024-01-09 15:30:00', 0, 0),
    ('Java Security Best Practices', 'Ensuring the security of Java applications requires adherence to best practices. Techniques such as input validation and secure coding guidelines help protect against common vulnerabilities.', 4, '2024-01-11 17:40:00', 0, 0),
    ('Java Servlets and JSP: Web Development', 'Servlets and JSP are fundamental technologies for Java web development. They enable the creation of dynamic and interactive web applications by processing requests and generating dynamic content.', 5, '2024-01-14 09:00:00', 0, 0);
 
 
#Comments:
INSERT INTO comments (content, post_id, user_id, created_on, likes, dislikes)
VALUES
    ('Great explanation of multithreading in Java!', 1, 7, '2024-02-05 12:30:00', 0, 0),
    ('I love how Spring simplifies Java development!', 2, 4, '2024-02-15 15:45:00', 0, 0),
    ('Design patterns make Java applications scalable.', 3, 8, '2024-02-20 10:20:00', 0, 0),
    ('Effective exception handling is crucial for robust Java apps.', 4, 5, '2024-02-03 14:00:00', 0, 0),
    ('Java Collections are indeed versatile and powerful.', 5, 9, '2024-02-18 06:10:00', 0, 0),
    ('Security practices are essential for Java applications.', 6, 3, '2024-02-08 07:50:00', 0, 0),
    ('JavaFX is amazing for building modern UIs!', 7, 6, '2024-02-21 09:15:00', 0, 0),
    ('Socket programming is a key skill for Java devs.', 8, 1, '2024-02-02 11:30:00', 0, 0),
    ('Optimizing code is crucial for Java performance.', 9, 10, '2024-02-12 13:40:00', 0, 0),
    ('JUnit is a must for ensuring code reliability!', 10, 2, '2024-03-01 15:00:00', 0, 0),
    ('Java Stream API simplifies data manipulation.', 11, 8, '2024-02-06 10:45:00', 0, 0),
    ('Building RESTful APIs with Java is exciting!', 12, 7, '2024-02-13 14:30:00', 0, 0),
    ('Concurrency challenges require careful consideration.', 13, 1, '2024-02-17 16:20:00', 0, 0),
    ('Java 17 features are indeed impressive!', 14, 6, '2024-02-20 08:45:00', 0, 0),
    ('Memory management is vital for Java applications.', 15, 8, '2024-02-07 11:10:00', 0, 0),
    ('Microservices with Spring Boot is the future!', 16, 2, '2024-02-19 13:55:00', 0, 0),
    ('Java 3D graphics programming opens new possibilities.', 17, 9, '2024-02-09 15:30:00', 0, 0),
    ('Java security best practices are a must-follow.', 18, 4, '2024-02-11 17:40:00', 0, 0),
    ('Java Servlets and JSP are fundamental for web development.', 19, 5, '2024-02-14 09:00:00', 0, 0),
    ('Great insights into Java programming!', 1, 7, '2024-02-28 12:30:00', 0, 0);