INSERT INTO hotels (name, description, brand, house_number, street, city, country, post_code, phone, email, check_in, check_out)
VALUES ('DoubleTree by Hilton Minsk', 'The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotels 20th floor ...', 'Hilton', 9, 'Pobediteley Avenue', 'Minsk', 'Belarus', '220004', '+375 17 309-80-00', 'doubletreeminsk.info@hilton.com', '14:00', '12:00');

INSERT INTO hotel_amenities (hotel_id, amenity) VALUES
(1, 'Free parking'),
(1, 'Free WiFi'),
(1, 'Non-smoking rooms'),
(1, 'Concierge'),
(1, 'On-site restaurant'),
(1, 'Fitness center'),
(1, 'Pet-friendly rooms'),
(1, 'Room service'),
(1, 'Business center'),
(1, 'Meeting rooms');