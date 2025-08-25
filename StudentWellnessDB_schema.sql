CREATE TABLE Counselors (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    availability VARCHAR(50)
);

CREATE TABLE Appointments (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    student_name VARCHAR(100) NOT NULL,
    counselor_id INT NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    status VARCHAR(20),
    FOREIGN KEY (counselor_id) REFERENCES Counselors(id)
);

CREATE TABLE Feedback (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    student_name VARCHAR(100),
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comments VARCHAR(255)
);

