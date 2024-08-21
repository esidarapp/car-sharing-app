DELETE FROM rentals
WHERE rental_date = '2024-08-01'
AND return_date = '2024-08-07'
AND actual_return_date IS NULL
AND car_id = 1
AND user_id = 1;

DELETE FROM rentals
WHERE rental_date = '2024-08-05'
AND return_date = '2024-08-06'
AND actual_return_date = '2024-08-12'
AND car_id = 2
AND user_id = 1;

DELETE FROM rentals
WHERE rental_date = '2024-08-10'
AND return_date = '2024-08-15'
AND actual_return_date = '2024-08-15'
AND car_id = 3
AND user_id = 2;

DELETE FROM rentals
WHERE rental_date = '2024-08-12'
AND return_date = '2024-08-20'
AND actual_return_date IS NULL
AND car_id = 4
AND user_id = 1;

DELETE FROM rentals
WHERE rental_date = '2024-08-15'
AND return_date = '2024-08-22'
AND actual_return_date IS NULL
AND car_id = 1
AND user_id = 2;