-- Create customer table
CREATE TABLE customer (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

-- Create Booking table
CREATE TABLE booking (
    id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL REFERENCES customer(id) ON DELETE CASCADE,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL
);

-- Insert sample customer
INSERT INTO customer (id, name, email) VALUES
(1, 'John Doe', 'john@example.com'),
(2, 'Jane Smith', 'jane@example.com'),
(3, 'Alice Johnson', 'alice@example.com'),
(4, 'Bob Brown', 'bob@example.com'),
(5, 'Charlie Davis', 'charlie@example.com'),
(6, 'Diana Evans', 'diana@example.com'),
(7, 'Frank Green', 'frank@example.com'),
(8, 'Grace Hall', 'grace@example.com'),
(9, 'Henry Ives', 'henry@example.com'),
(10, 'Ivy Jones', 'ivy@example.com');

-- Insert sample booking
INSERT INTO booking (customer_id, check_in_date, check_out_date, status) VALUES
(1, '2024-11-01', '2024-11-05', 'BOOKED'),
(2, '2024-11-03', '2024-11-06', 'CANCELLED'),
(3, '2024-11-07', '2024-11-10', 'BOOKED'),
(4, '2024-11-12', '2024-11-15', 'BOOKED'),
(5, '2024-11-15', '2024-11-18', 'BOOKED'),
(6, '2024-11-18', '2024-11-20', 'CANCELLED'),
(7, '2024-11-20', '2024-11-25', 'BOOKED'),
(8, '2024-11-22', '2024-11-26', 'BOOKED'),
(9, '2024-11-25', '2024-11-28', 'CANCELLED'),
(10, '2024-11-28', '2024-12-01', 'BOOKED');
