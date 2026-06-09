-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jun 02, 2026 at 09:35 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `blackspacedb`
--

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `catimages` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `name`, `catimages`) VALUES
(18, 'Automotive', 'Automotive.png'),
(24, 'Baby, Kids & Toys', 'Baby, Kids & Toys.jpg'),
(25, 'Beauty & Personal Care', 'Beauty & Personal Care.jpg'),
(26, 'Digital Products', 'Digital Products.png'),
(27, 'Electronics', 'Electronics.jpg'),
(28, 'Fashion & Apparel', 'Fashion & Apparel.jpg'),
(29, 'Groceries & Food', 'Groceries & Food.jpg'),
(30, 'Hardware & Tools', 'Hardware & Tools.png'),
(31, 'Health & Wellness', 'Health & Wellness.jpg'),
(32, 'Home & Living', 'Home & Living.jpg'),
(33, 'Jewelry & Accessories', 'Jewelry & Accessories.png'),
(34, 'Media', 'Media.png'),
(35, 'Office, Books & Stationery', 'Office, Books & Stationery.jpg'),
(36, 'Pets & Pet Supplies', 'Pets & Pet Supplies.jpg'),
(37, 'Real Estate & Housing', 'Real Estate & Housing.jpg'),
(38, 'Services', 'Services.png'),
(39, 'Sports & Outdoors.jpg', 'Sports & Outdoors.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `memberstoresubscription`
--

CREATE TABLE `memberstoresubscription` (
  `id` bigint(20) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `subscribed_at` datetime(6) DEFAULT NULL,
  `store_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `memberstoresubscription`
--

INSERT INTO `memberstoresubscription` (`id`, `active`, `email`, `subscribed_at`, `store_id`) VALUES
(8, b'1', 'zulusteven9@gmail.com', '2026-01-07 20:44:14.000000', 49),
(9, b'1', 'szulu@workers.com.zm', '2026-01-07 21:51:55.000000', 49),
(10, b'1', 'immanuelzulu50@gmail.com', '2026-01-11 14:29:13.000000', 49);

-- --------------------------------------------------------

--
-- Table structure for table `password_reset_token`
--

CREATE TABLE `password_reset_token` (
  `id` bigint(20) NOT NULL,
  `expiry_date` datetime(6) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `id` bigint(20) NOT NULL,
  `amount` varchar(255) DEFAULT NULL,
  `createdtime` datetime(6) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `payment_type` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `transaction_reference` varchar(255) DEFAULT NULL,
  `subscriptionid` varchar(255) DEFAULT NULL,
  `enabled` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`id`, `amount`, `createdtime`, `username`, `payment_method`, `payment_type`, `status`, `transaction_reference`, `subscriptionid`, `enabled`) VALUES
(41, '20', '2026-01-07 18:29:17.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'UPGRADED', 'Ideal For: Small sellers just starting out', '1', 0),
(42, '50', '2026-01-07 18:30:33.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'UPGRADED', 'Ideal For: Growing sellers who want to expand their store', '2', 0),
(43, '20', '2026-01-07 18:32:01.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'UPGRADED', 'Ideal For: Small sellers just starting out', '1', 0),
(44, '20', '2026-01-07 18:42:17.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'UPGRADED', 'Ideal For: Small sellers just starting out', '1', 0),
(45, '300', '2026-01-07 18:45:45.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'UPGRADED', 'Ideal For: Medium-sized stores looking for better value', '3', 0),
(46, '20', '2026-01-07 18:46:44.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'UPGRADED', 'Ideal For: Small sellers just starting out', '1', 0),
(47, '20', '2026-01-07 21:49:29.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'UPGRADED', 'Ideal For: Small sellers just starting out', '1', 0),
(48, '20', '2026-01-07 21:50:31.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'UPGRADED', 'Ideal For: Small sellers just starting out', '1', 0),
(49, '50', '2026-01-08 11:10:35.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'UPGRADED', 'Ideal For: Growing sellers who want to expand their store', '2', 0),
(50, '20', '2026-01-08 13:08:12.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'UPGRADED', 'Ideal For: Small sellers just starting out', '1', 0),
(51, '50', '2026-01-11 14:36:32.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'UPGRADED', 'Ideal For: Growing sellers who want to expand their store', '2', 0),
(52, '20', '2026-01-11 15:08:22.000000', 'izulu', 'UPGRADE', 'SUBSCRIPTION', 'ACTIVE', 'Ideal For: Small sellers just starting out', '1', 1),
(53, '20', '2026-01-21 13:32:23.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'UPGRADED', 'Ideal For: Small sellers just starting out', '1', 0),
(54, '50', '2026-01-21 14:31:10.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'ACTIVE', 'Ideal For: Growing sellers who want to expand their store', '2', 1);

-- --------------------------------------------------------

--
-- Table structure for table `productstock`
--

CREATE TABLE `productstock` (
  `id` bigint(20) NOT NULL,
  `audiourl` varchar(255) DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` longtext DEFAULT NULL,
  `dislikes` int(11) DEFAULT NULL,
  `imageurl` varchar(255) DEFAULT NULL,
  `likes` int(11) DEFAULT NULL,
  `name` varchar(150) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `shortdescription` text DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `imageurls` text DEFAULT NULL,
  `storeid` varchar(100) DEFAULT NULL,
  `views` bigint(20) DEFAULT NULL,
  `rating_count` int(11) DEFAULT NULL,
  `rating_total` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `productstock`
--

INSERT INTO `productstock` (`id`, `audiourl`, `category`, `created_at`, `description`, `dislikes`, `imageurl`, `likes`, `name`, `price`, `shortdescription`, `updated_at`, `username`, `currency`, `location`, `imageurls`, `storeid`, `views`, `rating_count`, `rating_total`) VALUES
(50, NULL, 'Fashion & Apparel', '2026-01-07 13:10:12.000000', 'Crocs Men\'s shoes combine lightweight comfort with durable design, perfect for everyday wear. Their iconic slip-on style, cushioned footbed, and breathable construction make them ideal for casual outings, work, or relaxing at home. Easy to clean and versatile, Crocs deliver all-day comfort without compromising style', 0, NULL, 1, 'Crocs Men\'s', '150', 'Crocs Men\'s', '2026-01-09 15:01:39.000000', 'szulu', 'ZMK', NULL, '19ebe14d-44af-4cd0-843f-e04fec9f3703_crocs.png,febac993-1a5c-4f9f-be19-03610486cf69_crocs2.png,98e0a94c-7499-4f69-b470-3181d7ee6b0c_crocs3.png,4c742eca-bfe7-4465-af74-2b50cd2e9142_crocs4.png', '49', 7, 6, 22),
(51, NULL, 'Electronics', '2026-01-08 13:10:12.000000', 'The Apple iPhone 17 Pro Max in Cosmic Orange color with 256 GB of storage capacity is a top-of-the-line smartphone from Apple. With its Hexa Core processor, Apple A19 Pro chipset, and 12 GB of RAM, this device offers powerful performance for all your needs. The 6.9-inch display and 48 MP camera ensure high-quality visuals and stunning photos. Additionally, with Xfinity network compatibility, USB Type-C connectivity, and various other features like Bluetooth, Wi-Fi, and NFC, the iPhone 17 Pro Max provides a seamless and convenient user experience.', 1, NULL, 1, 'Apple iPhone 17 Pro Max - 256 GB', '12000', 'Apple iPhone 17 Pro Max - 256 GB - Cosmic Orange (Xfinity)', '2026-05-26 09:39:35.000000', 'szulu', 'ZMK', NULL, '4404caa6-7884-44f4-b1d4-13e0c3e2289a_iphone17promax.png,f5db4332-e35a-4b7b-8d80-088bffff9d82_iphone17promax2.png,9386fd50-c486-4e23-b388-49551baea68a_iphone17promax3.png,3285049f-e19b-4a12-93aa-29890bdb9226_iphone17promax4.png', '49', 14, 0, 0),
(52, NULL, 'Fashion & Apparel', '2026-01-08 13:10:12.000000', 'Chanel 5422 Sunglasses in Black offer timeless elegance with premium craftsmanship. Featuring sleek frames and UV-protective lenses, they provide both style and eye protection. Perfect for elevating any outfit with a touch of luxury and sophistication.', 0, NULL, 2, 'Chanel 5422 Sunglasses Black', '350', 'Chanel 5422 Sunglasses Black 1026/s4 Square W/gray Lens 5422b! Ships Today!', '2026-05-26 09:39:59.000000', 'szulu', 'ZMK', NULL, '2c82630a-44ec-498b-b12c-1b2528d008fa_sunglasses.png,01bbd3fa-16eb-493f-8b92-5f0ae6914ab8_sunglasses2.png,603ef93a-97f5-4860-b076-d6e333b89e77_sunglasses3.png', '49', 7, 1, 3),
(53, NULL, 'Fashion & Apparel', '2026-01-08 14:13:04.000000', 'New without tags: This item is brand new and has never been worn, but doesn’t have tags and/or is missing the original packaging.', 0, NULL, 1, 'New Women\'s The North Face Campshire Coat', '12', 'New Women\'s The North Face Campshire Coat Top Fleece 2.0 Pullover Hoodie Jacket', '2026-05-22 09:21:40.000000', 'szulu', 'ZMK', NULL, '1bb873ba-9eec-4cce-8331-9faeaa167645_coat1.png,b9c21ac5-803d-41d7-9ca3-d3f87e574e37_coat2.png,23077b2d-53eb-4d69-838d-10e6d7cff4df_coat3.png', '49', 5, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `store`
--

CREATE TABLE `store` (
  `id` bigint(20) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` longtext DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `storebanner` varchar(255) DEFAULT NULL,
  `storeid` varchar(255) DEFAULT NULL,
  `storelogo` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `useridimage` varchar(255) DEFAULT NULL,
  `contact_number` varchar(255) DEFAULT NULL,
  `shopviews` varchar(255) DEFAULT NULL,
  `whatsapp_number` varchar(255) DEFAULT NULL,
  `numberofproducts` varchar(255) DEFAULT NULL,
  `durationindays` varchar(255) DEFAULT NULL,
  `durationtimeupdate` datetime(6) DEFAULT NULL,
  `numofproducts` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `store`
--

INSERT INTO `store` (`id`, `active`, `created_at`, `description`, `email`, `name`, `storebanner`, `storeid`, `storelogo`, `updated_at`, `username`, `useridimage`, `contact_number`, `shopviews`, `whatsapp_number`, `numberofproducts`, `durationindays`, `durationtimeupdate`, `numofproducts`) VALUES
(49, b'1', '2026-01-07 16:56:26.000000', 'About Us\r\n\r\nWelcome to BlackSpace Electronics, your trusted online destination for quality electronics and smart technology. We specialize in providing reliable gadgets, accessories, and electronic solutions designed to make everyday life easier and more connected.\r\n\r\nOur mission is to offer authentic products, competitive prices, and excellent customer service. Every item in our store is carefully selected to ensure quality, durability, and value for money.\r\n\r\nAt New Day Online Store , we believe technology should be accessible, dependable, and simple to use. Whether you’re upgrading your home, office, or personal devices, we’re here to help you make the right choice.', 'zulusteven9@gmail.com', 'New Day Online Store ', '1767797802112_banner1.jpg', 'STORE20260107_8620324', '1767797786204_logo1.jpg', '2026-01-21 14:31:10.000000', 'szulu', NULL, '0973297566', NULL, '0973297566', '12', '21', '2026-02-11 14:31:10.000000', NULL),
(50, b'1', '2026-01-11 15:07:18.000000', 'Loans', 'immanuelzulu50@gmail.com', 'Impact Makers Foundation (IMF)', '1767797802112_banner1.jpg', 'STORE20260111_3878411', '1767797786204_logo1.jpg', '2026-01-11 15:08:22.000000', 'izulu', NULL, '0976606318', NULL, '0976606318', '3', '7', '2026-01-18 15:08:22.000000', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `subscription`
--

CREATE TABLE `subscription` (
  `id` bigint(20) NOT NULL,
  `amount` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `durationtime` varchar(255) DEFAULT NULL,
  `numberofproducts` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `subscription`
--

INSERT INTO `subscription` (`id`, `amount`, `description`, `name`, `durationtime`, `numberofproducts`) VALUES
(1, '20', 'Ideal For: Small sellers just starting out', 'Starter Plan', '7', '3'),
(2, '50', 'Ideal For: Growing sellers who want to expand their store', 'Basic Plan', '21', '12'),
(3, '300', 'Ideal For: Medium-sized stores looking for better value', 'Standard Plan', '120', '50'),
(4, '800', 'Ideal For: Large stores wanting a full year subscription', 'Professional Plan', '365', '100'),
(5, '2', 'test prices products', 'Premium', '7', '3');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `town` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `docidimage` varchar(255) DEFAULT NULL,
  `docidnumber` varchar(255) DEFAULT NULL,
  `userID` varchar(255) DEFAULT NULL,
  `otp_enabled` bit(1) DEFAULT NULL,
  `otp_secret` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `address`, `country`, `created_at`, `email`, `enabled`, `first_name`, `last_name`, `password`, `phone_number`, `role`, `status`, `town`, `updated_at`, `username`, `docidimage`, `docidnumber`, `userID`, `otp_enabled`, `otp_secret`) VALUES
(1, 'ndola zambia', 'zambia', '2025-12-23 20:55:06.000000', 'szulu@workers.com.zm', b'1', 'stephen', 'zulu', '$2a$10$vSQexPanjStB83u.kj8szORk8mocCY7yrYwswhR2aDPUCoqwppwWG', '0973297567', 'ADMIN', '1', 'ndola', '2025-12-23 20:55:06.000000', 'szulu1', NULL, '35434', '1001', NULL, NULL),
(15, 'ndola', 'Zambia', '2026-01-02 18:06:13.000000', 'zulusteven9@gmail.com', b'1', 'stephen ', 'zulu', '$2a$10$U4wyZIJu4dr0TK4ezHW.T.9awooqsrqQDvrvYDOs2XCkuC4hN8w7e', '0973297566', 'SELLER', '1', 'ndola', '2026-01-03 12:44:28.000000', 'szulu', NULL, '35434', NULL, b'0', NULL),
(18, 'ndola', 'Zambia', '2026-01-11 14:56:07.000000', 'immanuelzulu50@gmail.com', b'1', 'immanuel', 'zulu', '$2a$10$SVR2ss0s/oM7cnOaoOfUGOrku.6q98E5tMJAdt9ztqaW9IYOPQ0qS', '0976606318', 'SELLER', '1', 'ndola', NULL, 'izulu', NULL, '24948454', NULL, b'0', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKt8o6pivur7nn124jehx7cygw5` (`name`);

--
-- Indexes for table `memberstoresubscription`
--
ALTER TABLE `memberstoresubscription`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKqrqffhx3xf477yq82uhe0uw1r` (`store_id`);

--
-- Indexes for table `password_reset_token`
--
ALTER TABLE `password_reset_token`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKf90ivichjaokvmovxpnlm5nin` (`user_id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `productstock`
--
ALTER TABLE `productstock`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `store`
--
ALTER TABLE `store`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `subscription`
--
ALTER TABLE `subscription`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKby8678y0av8o0t5xtgg1gak2w` (`name`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK4bgmpi98dylab6qdvf9xyaxu4` (`phone_number`),
  ADD UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT for table `memberstoresubscription`
--
ALTER TABLE `memberstoresubscription`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `password_reset_token`
--
ALTER TABLE `password_reset_token`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;

--
-- AUTO_INCREMENT for table `productstock`
--
ALTER TABLE `productstock`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=54;

--
-- AUTO_INCREMENT for table `store`
--
ALTER TABLE `store`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT for table `subscription`
--
ALTER TABLE `subscription`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `memberstoresubscription`
--
ALTER TABLE `memberstoresubscription`
  ADD CONSTRAINT `FKqrqffhx3xf477yq82uhe0uw1r` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`);

--
-- Constraints for table `password_reset_token`
--
ALTER TABLE `password_reset_token`
  ADD CONSTRAINT `FK5lwtbncug84d4ero33v3cfxvl` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
