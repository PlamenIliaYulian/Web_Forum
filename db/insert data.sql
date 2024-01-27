
#Users:
insert into web_forum_database.users (user_id, user_email, password, first_name, last_name, is_deleted, is_blocked, username) values (1, 'john.doe@example.com', 'AbCdEfGhIjKlMnOpQrSt', 'John', 'Doe', 0, 0, 'john_doe');
insert into web_forum_database.users (user_id, user_email, password, first_name, last_name, is_deleted, is_blocked, username) values (2, 'jane.smith@example.com', 'XyZaBcDeFgHiJkLmNoP', 'Jane', 'Smith', 0, 0, 'jane_smith');
insert into web_forum_database.users (user_id, user_email, password, first_name, last_name, is_deleted, is_blocked, username) values (3, 'michael.jones@example.com', 'PqRsTuVwXyZaBcDeFgH', 'Michael', 'Jones', 0, 0, 'michael_jones');
insert into web_forum_database.users (user_id, user_email, password, first_name, last_name, is_deleted, is_blocked, username) values (4, 'susan.miller@example.com', 'IjKlMnOpQrStUvWxYzA', 'Susan', 'Miller', 0, 0, 'susan_miller');
insert into web_forum_database.users (user_id, user_email, password, first_name, last_name, is_deleted, is_blocked, username) values (5, 'robert.white@example.com', 'HgFeDcBa0987654321', 'Robert', 'White', 0, 0, 'robert_white');
insert into web_forum_database.users (user_id, user_email, password, first_name, last_name, is_deleted, is_blocked, username) values (6, 'laura.wilson@example.com', 'LmNoPqRsTuVwXyZaBcD', 'Laura', 'Wilson', 0, 0, 'laura_wilson');
insert into web_forum_database.users (user_id, user_email, password, first_name, last_name, is_deleted, is_blocked, username) values (7, 'david.brown@example.com', 'MnOpQrStUvWxYzAbCdEf', 'David', 'Brown', 0, 0, 'david_brown');
insert into web_forum_database.users (user_id, user_email, password, first_name, last_name, is_deleted, is_blocked, username) values (8, 'emily.jackson@example.com', 'KlMnOpQrStUvWxYzAbC', 'Emily', 'Jackson', 0, 0, 'emily_jackson');
insert into web_forum_database.users (user_id, user_email, password, first_name, last_name, is_deleted, is_blocked, username) values (9, 'peter.anderson@example.com', 'XyZaBcDeFgHiJkLmNoPq', 'Peter', 'Anderson', 0, 0, 'peter_anderson');
insert into web_forum_database.users (user_id, user_email, password, first_name, last_name, is_deleted, is_blocked, username) values (10, 'natalie.hall@example.com', 'HgFeDcBa0987654321', 'Natalie', 'Hall', 0, 0, 'natalie_hall');

#Roles:
insert into web_forum_database.roles (role_id, name) values (1, 'ROLE_ADMIN');
insert into web_forum_database.roles (role_id, name) values (2, 'ROLE_MEMBER');
insert into web_forum_database.roles (role_id, name) values (3, 'ROLE_STAFF_MEMBER');


#Tags:
insert into web_forum_database.tags (tag_id, name, is_deleted) values (1, 'JavaBasics', 0);
insert into web_forum_database.tags (tag_id, name, is_deleted) values (2, 'OOPJava', 0);
insert into web_forum_database.tags (tag_id, name, is_deleted) values (3, 'JavaCollections', 0);
insert into web_forum_database.tags (tag_id, name, is_deleted) values (4, 'JavaThreads', 0);
insert into web_forum_database.tags (tag_id, name, is_deleted) values (5, 'JavaExceptions', 0);
insert into web_forum_database.tags (tag_id, name, is_deleted) values (6, 'JavaIO', 0);
insert into web_forum_database.tags (tag_id, name, is_deleted) values (7, 'JavaNetworking', 0);
insert into web_forum_database.tags (tag_id, name, is_deleted) values (8, 'JavaSwing', 0);
insert into web_forum_database.tags (tag_id, name, is_deleted) values (9, 'JavaFX', 0);
insert into web_forum_database.tags (tag_id, name, is_deleted) values (10, 'SpringFramework', 0);
insert into web_forum_database.tags (tag_id, name, is_deleted) values (11, 'HibernateORM', 0);
insert into web_forum_database.tags (tag_id, name, is_deleted) values (12, 'JavaTesting', 0);
insert into web_forum_database.tags (tag_id, name, is_deleted) values (13, 'JavaWebDevelopment', 0);
insert into web_forum_database.tags (tag_id, name, is_deleted) values (14, 'JavaBestPractices', 0);


#Posts:
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (1, 'Understanding Java Multithreading', 'Multithreading is a crucial aspect of Java programming, allowing concurrent execution of tasks. It enhances performance by efficiently utilizing system resources.', 3, '2024-01-25 10:20:09', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (2, 'Exploring Java Spring Framework', 'The Spring Framework simplifies Java development by providing comprehensive infrastructure support. With features like dependency injection, it promotes modular and testable code.', 7, '2024-01-23 06:34:03', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (3, 'Java Design Patterns for Scalability', 'Applying design patterns is essential for building scalable Java applications. Patterns like Singleton and Observer contribute to creating robust and efficient systems.', 1, '2024-01-23 06:34:04', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (4, 'Effective Exception Handling in Java', 'Exception handling is a critical part of Java programming. By using try-catch blocks, developers can manage runtime errors and ensure graceful application behavior.', 8, '2024-01-23 06:34:03', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (5, 'Mastering Java Collections Framework', 'Java Collections offer a versatile set of data structures. From Lists to Maps, understanding and utilizing these classes is key for efficient data manipulation.', 6, '2024-01-23 06:34:03', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (6, 'Secure Coding Practices in Java', 'Security is paramount in Java development. Adhering to secure coding practices helps prevent vulnerabilities and ensures the integrity of your applications.', 2, '2024-01-23 06:34:04', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (7, 'JavaFX: Building Modern User Interfaces', 'JavaFX provides a platform for creating rich, interactive user interfaces. With features like FXML and CSS styling, developers can build modern and visually appealing applications.', 9, '2024-01-23 06:34:04', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (8, 'Java Networking: Socket Programming', 'Understanding socket programming is essential for building networked applications in Java. It enables communication between distributed systems through the use of sockets.', 4, '2024-01-23 06:34:03', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (9, 'Optimizing Java Code for Performance', 'Performance optimization is a continuous process in Java development. Techniques like code profiling and efficient algorithm implementation contribute to faster and more responsive applications.', 5, '2024-01-23 06:34:03', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (10, 'Unit Testing in Java with JUnit', 'JUnit is a powerful testing framework for Java. Adopting unit testing practices ensures the reliability and correctness of your code throughout the development process.', 10, '2024-01-23 06:34:03', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (11, 'Java Stream API: Functional Programming', 'The Stream API in Java facilitates functional programming paradigms. Leveraging streams allows developers to write concise and expressive code for data manipulation.', 3, '2024-01-23 06:34:03', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (12, 'Developing RESTful APIs with Java', 'Building RESTful APIs is a common requirement in Java development. Technologies like Spring Boot simplify the process, enabling developers to create scalable and robust APIs.', 7, '2024-01-23 06:34:03', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (13, 'Concurrency Challenges in Java Programming', 'Concurrency introduces challenges in Java development. Addressing issues such as race conditions and deadlock is crucial for building stable and reliable concurrent applications.', 1, '2024-01-23 06:34:04', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (14, 'Java 17 Features: What is New?', 'Java 17 introduces new features and enhancements. From pattern matching to sealed classes, staying updated on the latest language improvements is essential for modern Java development.', 6, '2024-01-23 06:34:04', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (15, 'Java Memory Management Best Practices', 'Effective memory management is vital for preventing memory leaks and optimizing Java applications. Understanding concepts like garbage collection contributes to efficient resource utilization.', 8, '2024-01-23 06:34:04', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (16, 'Building Microservices with Spring Boot', 'Microservices architecture is a popular approach in Java development. Using Spring Boot, developers can create modular and scalable microservices for building robust distributed systems.', 2, '2024-01-23 06:34:04', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (17, 'Java 3D Graphics Programming', 'Java supports 3D graphics programming through libraries like JOGL. Developing applications with 3D visualization enhances the user experience and opens up possibilities for innovative interfaces.', 9, '2024-01-23 06:34:04', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (18, 'Java Security Best Practices', 'Ensuring the security of Java applications requires adherence to best practices. Techniques such as input validation and secure coding guidelines help protect against common vulnerabilities.', 4, '2024-01-23 06:34:04', 0, 0, 0);
insert into web_forum_database.posts (post_id, title, content, user_id, created_on, is_deleted, likes, dislikes) values (19, 'Java Servlets and JSP: Web Development', 'Servlets and JSP are fundamental technologies for Java web development. They enable the creation of dynamic and interactive web applications by processing requests and generating dynamic content.', 5, '2024-01-23 06:34:04', 0, 0, 0);

#Users_Roles
insert into web_forum_database.users_roles (user_id, role_id) values (1, 1);


#Comments:
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (1, 'Great explanation of multithreading in Java!', 0, 7, '2024-01-25 09:06:45', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (2, 'I love how Spring simplifies Java development!', 0, 4, '2024-01-23 06:54:35', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (3, 'Design patterns make Java applications scalable.', 0, 8, '2024-01-23 06:54:35', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (4, 'Effective exception handling is crucial for robust Java apps.', 0, 5, '2024-01-23 06:54:35', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (5, 'Java Collections are indeed versatile and powerful.', 0, 9, '2024-01-23 06:54:35', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (6, 'Security practices are essential for Java applications.', 0, 3, '2024-01-23 06:54:35', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (7, 'JavaFX is amazing for building modern UIs!', 0, 6, '2024-01-23 06:54:35', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (8, 'Socket programming is a key skill for Java devs.', 0, 1, '2024-01-23 06:54:35', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (9, 'Optimizing code is crucial for Java performance.', 0, 10, '2024-01-23 06:54:35', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (10, 'JUnit is a must for ensuring code reliability!', 0, 2, '2024-01-23 06:54:35', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (11, 'Java Stream API simplifies data manipulation.', 0, 8, '2024-01-23 06:54:35', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (12, 'Building RESTful APIs with Java is exciting!', 0, 7, '2024-01-23 06:54:35', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (13, 'Concurrency challenges require careful consideration.', 0, 1, '2024-01-23 06:54:35', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (14, 'Java 17 features are indeed impressive!', 0, 6, '2024-01-23 06:54:35', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (15, 'Memory management is vital for Java applications.', 0, 8, '2024-01-23 06:54:34', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (16, 'Microservices with Spring Boot is the future!', 0, 2, '2024-01-23 06:54:35', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (17, 'Java 3D graphics programming opens new possibilities.', 0, 9, '2024-01-23 06:54:34', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (18, 'Java security best practices are a must-follow.', 0, 4, '2024-01-23 06:54:35', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (19, 'Java Servlets and JSP are fundamental for web development.', 0, 5, '2024-01-23 06:54:34', 0, 0);
insert into web_forum_database.comments (comment_id, content, is_deleted, user_id, created_on, likes, dislikes) values (20, 'Great insights into Java programming!', 0, 7, '2024-01-23 06:54:34', 0, 0);
