-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jan 04, 2026 at 06:23 PM
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
(4, 'Sports & Outdoors', 'logo.jpeg'),
(9, 'Jewelry & Accessories', 'logo.jpeg'),
(13, 'Fashion & Apparel', 'logo.jpeg'),
(15, 'Electronics', 'logo.jpeg'),
(18, 'Automotive', 'logo.jpeg'),
(19, 'Health & Wellness', 'logo.jpeg'),
(20, 'Services', '1767451028628_crocks.png'),
(22, 'Media', 'logo.jpeg'),
(23, 'Real Estate & Housing', '1767450993694_teams.png');

-- --------------------------------------------------------

--
-- Table structure for table `membership`
--

CREATE TABLE `membership` (
  `id` bigint(20) NOT NULL,
  `amount` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `duration` varchar(255) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `end_date` date NOT NULL,
  `start_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `membership`
--

INSERT INTO `membership` (`id`, `amount`, `description`, `duration`, `name`, `end_date`, `start_date`) VALUES
(1, '20', 'fhgvdf\r\ndfhdfghgd\r\ndfhgdhfg\r\nhghgdf\r\nhgdfgh\r\nhgdfhdgf\r\njhdsdb\r\nhjbehjb', '2', 'Premium', '2025-12-24', '2025-12-22'),
(2, '20', '23', '2', 'Gold', '2025-12-23', '2025-12-22'),
(3, '20', 'dekjddkndjkdf', NULL, 'Premium Coffee Maker', '2026-12-09', '2025-12-22'),
(4, '20', '23232323ddfdf', NULL, 'stephen zulu', '2026-01-23', '2025-12-09'),
(5, '36', 'dffdfdfdf', NULL, 'Premium Cotton T-Shirt', '2025-12-28', '2025-12-23'),
(6, '20', '203', NULL, 'platinum', '2025-12-25', '2025-12-09');

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
  `membershipid` varchar(255) DEFAULT NULL,
  `enabled` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`id`, `amount`, `createdtime`, `username`, `payment_method`, `payment_type`, `status`, `transaction_reference`, `subscriptionid`, `membershipid`, `enabled`) VALUES
(24, '20', '2025-12-31 18:16:27.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'UPGRADED', 'desdhgvajhbc bnahbdksubf dhjbfkjgdf sjkfdhjad', '1', NULL, 0),
(25, '20', '2025-12-31 18:16:43.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'ACTIVE', 'ddsdsdsd', '2', NULL, 0),
(26, '1', '2025-12-31 22:29:44.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'UPGRADED', 'wewe', '3', NULL, 0),
(30, '20', '2026-01-01 01:13:18.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'UPGRADED', 'desaggffg', '4', NULL, 0),
(31, '20', '2026-01-01 01:17:44.000000', 'szulu', 'UPGRADE', 'SUBSCRIPTION', 'ACTIVE', 'desaggffg', '4', NULL, 1),
(32, '20', '2026-01-01 01:17:44.000000', 'szulu2', 'UPGRADE', 'SUBSCRIPTION', 'ACTIVE', 'desaggffg', '4', NULL, 1);

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
  `views` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `productstock`
--

INSERT INTO `productstock` (`id`, `audiourl`, `category`, `created_at`, `description`, `dislikes`, `imageurl`, `likes`, `name`, `price`, `shortdescription`, `updated_at`, `username`, `currency`, `location`, `imageurls`, `storeid`, `views`) VALUES
(1, '89ea50c1-4061-4197-85af-004d76e7cbf8_Dax ft. Lewis Capaldi - Note to self  Lyrics Video - Coco (youtube).mp3', 'Fashion & Apparel', '2024-01-15 10:30:00.000000', 'High-quality wireless headphones with noise cancellation and 30-hour battery life', 12, '45707b1f-195f-4970-b8a6-d52d37fc512f_zoom.png', 156, 'Premium product1111', '199', 'Noise-cancelling', '2026-01-04 17:46:31.000000', '1001', 'ZMK', 'Los Angeles', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_03', 182),
(2, '89ea50c1-4061-4197-85af-004d76e7cbf8_Dax ft. Lewis Capaldi - Note to self  Lyrics Video - Coco (youtube).mp3', 'Clothes', '2024-01-16 09:15:00.000000', '100% cotton t-shirt with premium print quality, available in multiple sizes', 5, '04fb3cee-81ed-4042-9d89-9e6139f55b96_Screenshot 2025-09-02 at 8.42.29 PM.png', 89, 'Premium Cotton T-Shirt', '29.99', 'Comfortable cotton t-shirt', '2026-01-04 17:46:06.000000', '1001', 'EUR', 'Los Angeles', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_03', 181),
(3, 'https://example.com/audio/book-preview.mp3', 'Annual Reports', '2024-01-17 14:20:00.000000', 'Bestselling fiction novel with captivating storyline and character development', 3, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 234, 'Mystery Novel: Hidden Truths', '15.99', 'Bestselling mystery fiction', '2025-12-28 08:55:50.000000', '1002', 'EUR', 'Los Angeles', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_03', 181),
(12, '0d275362-1852-4e9f-b821-a4beb71c8208_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Service Charter', '2025-12-18 22:04:39.000000', 'Black Space Online Store is your go-to digital marketplace for discovering a wide range of high-quality products at unbeatable prices. Our platform connects buyers and sellers seamlessly, offering a convenient, secure, and user-friendly shopping experience.\n\nAt Black Space, we are committed to providing exceptional customer service and ensuring every transaction is smooth and trustworthy. From electronics and fashion to home essentials and unique finds, we aim to bring the best products directly to your fingertips.\n\nOur mission is to create a space where shopping meets convenience, quality, and value, empowering both sellers to grow their businesses and buyers to discover products they love. With Black Space Online Store, your next purchase is just a click away.', 3, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 33, 'Tesla Model X', '199.99', 'Noise-cancelling wireless headphones', '2025-12-28 08:59:55.000000', '1001', 'EUR', 'Los Angeles', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_03', 179),
(13, '1c90da1c-f6d9-4130-bd37-0afd07bfc26d_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Compliance Benefits', '2025-12-18 22:11:37.000000', 'gfhbh', 2, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 32, 'Premium', '199.99', 'Programmable coffee machine', '2025-12-20 02:14:31.000000', '1001', 'EUR', 'New York', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_03', 179),
(14, 'e6c3f143-e163-41f7-abef-645b8e768199_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Jewelry & Accessories', '2025-12-18 22:49:53.000000', 'gvg fgvfgv', 4, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 48, 'Premium', '199.99', 'Programmable coffee machine', '2025-12-28 08:55:11.000000', '1001', 'EUR', 'Los Angeles', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_03', 190),
(15, '61a2b92a-59fb-4d3b-8b57-00eb0940899c_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Annual Reports', '2025-12-18 22:56:50.000000', 'gvyvyg', 2, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 33, 'Premium', '199.99', 'Bestselling mystery fiction', '2026-01-04 15:15:16.000000', '1002', 'GBP', 'New York', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 183),
(16, 'e6c3f143-e163-41f7-abef-645b8e768199_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Jewelry & Accessories', '2025-12-18 22:49:53.000000', 'gvg fgvfgv', 3, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 44, 'Premium', '199.99', 'Programmable coffee machine', '2025-12-28 09:02:31.000000', '1002', 'EUR', 'Los Angeles', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 181),
(17, '61a2b92a-59fb-4d3b-8b57-00eb0940899c_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Jewelry & Accessories', '2025-12-18 22:56:50.000000', 'gvyvyg', 2, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 33, 'Premium', '199.99', 'Bestselling mystery fiction', '2025-12-28 09:21:19.000000', '1002', 'GBP', 'New York', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 182),
(18, '0d275362-1852-4e9f-b821-a4beb71c8208_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Service Charter', '2025-12-18 22:04:39.000000', 'Black Space Online Store is your go-to digital marketplace for discovering a wide range of high-quality products at unbeatable prices. Our platform connects buyers and sellers seamlessly, offering a convenient, secure, and user-friendly shopping experience.\r\n\r\nAt Black Space, we are committed to providing exceptional customer service and ensuring every transaction is smooth and trustworthy. From electronics and fashion to home essentials and unique finds, we aim to bring the best products directly to your fingertips.\r\n\r\nOur mission is to create a space where shopping meets convenience, quality, and value, empowering both sellers to grow their businesses and buyers to discover products they love. With Black Space Online Store, your next purchase is just a click away.', 3, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 33, 'Tesla Model X', '199.99', 'Noise-cancelling wireless headphones', '2025-10-07 20:15:31.000000', '1002', 'EUR', 'Los Angeles', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 178),
(19, '1c90da1c-f6d9-4130-bd37-0afd07bfc26d_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Jewelry & Accessories', '2025-12-18 22:11:37.000000', 'gfhbh', 2, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 32, 'Premium', '199.99', 'Programmable coffee machine', '2025-12-20 02:07:10.000000', '1001', 'EUR', 'New York', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 178),
(21, '61a2b92a-59fb-4d3b-8b57-00eb0940899c_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Jewelry & Accessories', '2025-12-18 22:56:50.000000', 'gvyvyg', 2, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 33, 'Premium', '199.99', 'Bestselling mystery fiction', '2025-12-20 02:23:39.000000', '1002', 'GBP', 'New York', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 179),
(22, 'e6c3f143-e163-41f7-abef-645b8e768199_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Jewelry & Accessories', '2025-12-18 22:49:53.000000', 'gvg fgvfgv', 3, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 44, 'Premium', '199.99', 'Programmable coffee machine', '2025-12-20 02:07:10.000000', '1001', 'EUR', 'Los Angeles', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 178),
(23, '61a2b92a-59fb-4d3b-8b57-00eb0940899c_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Annual Reports', '2025-12-18 22:56:50.000000', 'gvyvyg', 2, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 33, 'Premium', '199.99', 'Bestselling mystery fiction', '2025-12-20 02:07:10.000000', '1001', 'GBP', 'New York', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 178),
(24, 'e6c3f143-e163-41f7-abef-645b8e768199_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Compliance Benefits', '2025-12-18 22:49:53.000000', 'gvg fgvfgv', 3, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 44, 'Premium', '199.99', 'Programmable coffee machine', '2025-12-20 01:41:40.000000', '1001', 'EUR', 'Los Angeles', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_02', 179),
(25, '61a2b92a-59fb-4d3b-8b57-00eb0940899c_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Compliance Benefits', '2025-12-18 22:56:50.000000', 'gvyvyg', 2, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 33, 'Premium', '199.99', 'Bestselling mystery fiction', '2025-12-25 20:44:27.000000', '1001', 'GBP', 'New York', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_02', 180),
(26, 'e6c3f143-e163-41f7-abef-645b8e768199_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Annual Reports', '2025-12-18 22:49:53.000000', 'gvg fgvfgv', 3, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 44, 'Premium', '199.99', 'Programmable coffee machine', '2025-12-20 02:07:10.000000', '1001', 'EUR', 'Los Angeles', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_02', 178),
(27, '61a2b92a-59fb-4d3b-8b57-00eb0940899c_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Compliance Benefits', '2025-12-18 22:56:50.000000', 'gvyvyg', 2, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 33, 'Premium', '199.99', 'Bestselling mystery fiction', '2025-12-28 08:57:01.000000', '1001', 'GBP', 'New York', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_03', 181),
(28, 'e6c3f143-e163-41f7-abef-645b8e768199_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Jewelry & Accessories', '2025-12-18 22:49:53.000000', 'gvg fgvfgv', 3, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 44, 'Premium', '199.99', 'Programmable coffee machine', '2025-12-20 02:07:10.000000', '1003', 'EUR', 'Los Angeles', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 178),
(31, '1c90da1c-f6d9-4130-bd37-0afd07bfc26d_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Jewelry & Accessories', '2025-12-18 22:11:37.000000', 'gfhbh', 2, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 32, 'Premium', '199.99', 'Programmable coffee machine', '2025-12-20 02:07:10.000000', '1002', 'EUR', 'New York', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 178),
(33, 'https://example.com/audio/book-preview.mp3', 'Clothes', '2024-01-17 14:20:00.000000', 'Bestselling fiction novel with captivating storyline and character development', 3, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 234, 'Mystery Novel: Hidden Truths', '15.99', 'Bestselling mystery fiction', '2025-12-20 15:52:23.000000', '1002', 'EUR', 'Los Angeles', '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_03', 180),
(34, '8d1337da-168d-4341-a1bb-1e5950bd1c03_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Clothes', '2025-12-29 09:48:03.000000', 'dfdfdf\r\ndfdfdf\r\ndfdfffffff\r\nggggggg\r\nhhhh', 0, '5efc0a4d-2e89-4866-9461-c59b7aa832ea_mlss.png', 0, 'tttttt', '123', 'ddfdfd', '2026-01-03 20:21:04.000000', '1001', 'ZMK', NULL, '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 2),
(35, '6e59b6f2-3785-43b9-93ca-b62ed2ec2d16_61a2b92a-59fb-4d3b-8b57-00eb0940899c_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Clothes', '2025-12-29 10:19:07.000000', 'Condition\r\nNew: A brand-new, unused, unopened, undamaged item in its original packaging (where packaging is ... Read moreabout the condition\r\nBrand\r\nApple\r\nScreen Size\r\n6.9 in\r\nNetwork\r\nUnlocked\r\nOperating System\r\niOS\r\nLock Status\r\nFactory Unlocked\r\nModel Number\r\nA3526 (Dual SIM, nano-SIM + eSIM)\r\nContract\r\nWithout Contract\r\nSIM Card Slot\r\nDual SIM (SIM + eSIM)\r\nCountry of Origin\r\nChina\r\nCategory\r\nbreadcrumb\r\nElectronics\r\nCell Phones & Accessories\r\nCell Phones & Smartphones', 0, NULL, 0, 'APPLE 2025 iPHONE 17 PRO MAX 6.9\" 256GB/512GB/1TB/2TB UNLOCKED (A3526 DUAL SIM)', '25000', 'APPLE 2025 iPHONE 17 PRO MAX 6.9\" 256GB/512GB/1TB/2TB UNLOCKED (A3526 DUAL SIM)', '2025-12-29 18:16:14.000000', NULL, 'ZMK', NULL, '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', NULL, 1),
(36, 'ea63ba7a-0e21-43f6-809d-e651f8872a2d_61a2b92a-59fb-4d3b-8b57-00eb0940899c_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', '22', '2025-12-29 10:24:38.000000', 'Condition\r\nNew with box: This item is brand new and has never been used. It still has the original tags and/or ... Read moreabout the condition\r\nBrand\r\nCrocs\r\nDepartment\r\nUnisex Adults\r\nStyle\r\nUnisex Clog\r\nUpper Material\r\nEVA (Ethylene Vinyl Acetate)\r\nPattern\r\nSolid\r\nFeatures\r\nLightweight, Comfort, Waterproof, Breathable, Ankle Strap, Vegan\r\nLining Material\r\nEVA (Ethylene Vinyl Acetate)\r\nInsole Material\r\nEVA (Ethylene Vinyl Acetate)\r\nOutsole Material\r\nEVA (Ethylene Vinyl Acetate)\r\nClosure\r\nSlip On\r\nModel\r\nCrocs Baya Clog\r\nProduct Line\r\nCrocs Baya\r\nOccasion\r\nCasual, Travel\r\nShoe Width\r\nMedium\r\nAccents\r\nLogo\r\nToe Shape\r\nRound Toe', 0, NULL, 0, 'Crocs Adult Baya Clogs', '199.99', 'Crocs Adult Baya Clogs', '2026-01-03 21:52:45.000000', '1001', 'ZMK', NULL, '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 1),
(37, NULL, 'Jewelry & Accessories', '2025-12-29 11:46:01.000000', 'These are Crocs Pollex Clog slippers by designer Salehe Bembury in gray.\r\n11 sizes', 0, NULL, 0, 'Crocs Men\'s', '199.99', 'These are Crocs Pollex Clog', '2026-01-03 22:55:47.000000', '1001', 'ZMK', NULL, '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 2),
(38, '5d690d6a-56ed-4a20-893b-b6059ce2ed4f_61a2b92a-59fb-4d3b-8b57-00eb0940899c_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Media', '2025-12-29 11:46:44.000000', 'Crocs Men\'s', 0, NULL, 0, 'Crocs Men\'s', '199.99', 'Crocs Men\'s', '2026-01-04 18:43:59.000000', '1001', 'GBP', NULL, '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 5),
(39, NULL, 'Electronics', '2025-12-31 20:19:31.000000', 'sdddhdjhbfjbfhd\r\nfgfgfgjhbjkbg\r\nhjbajdhbhjbvs\r\njhdkbfkhbdf\r\nhbjhbjhbdf', 0, NULL, 0, 'stephen zulu', '11', 'dsdsd', '2026-01-04 18:42:46.000000', '1001', 'ZMK', NULL, '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 4),
(40, 'ea63ba7a-0e21-43f6-809d-e651f8872a2d_61a2b92a-59fb-4d3b-8b57-00eb0940899c_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', '22', '2025-12-29 10:24:38.000000', 'Condition\r\nNew with box: This item is brand new and has never been used. It still has the original tags and/or ... Read moreabout the condition\r\nBrand\r\nCrocs\r\nDepartment\r\nUnisex Adults\r\nStyle\r\nUnisex Clog\r\nUpper Material\r\nEVA (Ethylene Vinyl Acetate)\r\nPattern\r\nSolid\r\nFeatures\r\nLightweight, Comfort, Waterproof, Breathable, Ankle Strap, Vegan\r\nLining Material\r\nEVA (Ethylene Vinyl Acetate)\r\nInsole Material\r\nEVA (Ethylene Vinyl Acetate)\r\nOutsole Material\r\nEVA (Ethylene Vinyl Acetate)\r\nClosure\r\nSlip On\r\nModel\r\nCrocs Baya Clog\r\nProduct Line\r\nCrocs Baya\r\nOccasion\r\nCasual, Travel\r\nShoe Width\r\nMedium\r\nAccents\r\nLogo\r\nToe Shape\r\nRound Toe', 0, NULL, 0, 'Crocs Adult Baya Clogs', '199.99', 'Crocs Adult Baya Clogs', '2026-01-03 21:52:45.000000', '1001', 'ZMK', NULL, '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 1),
(41, NULL, 'Jewelry & Accessories', '2025-12-29 11:46:01.000000', 'These are Crocs Pollex Clog slippers by designer Salehe Bembury in gray.\r\n11 sizes', 0, NULL, 0, 'Crocs Men\'s', '199.99', 'These are Crocs Pollex Clog', '2026-01-03 21:30:15.000000', '1001', 'ZMK', NULL, '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 1),
(42, '5d690d6a-56ed-4a20-893b-b6059ce2ed4f_61a2b92a-59fb-4d3b-8b57-00eb0940899c_Blaqmajor - Ke December (feat Mzamo Ngcobo , Gwill & DJ Kap) Official Music Video.mp3', 'Media', '2025-12-29 11:46:44.000000', 'Crocs Men\'s', 0, NULL, 0, 'Crocs Men\'s', '199.99', 'Crocs Men\'s', '2026-01-03 21:30:20.000000', '1001', 'GBP', NULL, '498ee10c-d7ea-4782-bae5-9e7e16e6f849_iphone.png,c1647df4-b146-4b75-b35b-0905a96a6ee0_WhatsApp Image 2025-11-07 at 15.45.59.jpeg,pair-of-colorful-sports-shoes-for-active-lifestyle-png.png,IMG_0992_efd0e8db-62b0-4d28-800f-b3bee9eae0ae.png,f03vh34v3b4-images.jpg,pngimg.com - mercedes_PNG80135-1.png,ghdhg4g8dv4g2gimages.jpg', 'BC_01', 3),
(43, '8bdb8196-297f-4b9a-a2a9-8c1fc167a1a0_Chile 84 ft Roberto & B Quan Phamaika -MARRY YOU (official audio).mp3', 'Media', '2026-01-04 15:56:00.000000', 'About this item\r\nUniversal unlocked: Compatible with all major U.S. carriers, including Verizon, AT&T, T-Mobile and other prepaid carriers.\r\nIconic design, now even more durable: Ultra-compact flip design featuring a stronger hinge plate made with titanium, beautiful finishes, and curated Pantone Colors.\r\nAccess what matters even when closed: Easily use your favorite apps on the 3.6\" external display, all without having to flip open the phone.\r\n50MP camera system powered by moto ai: Capture stunning photos in any light and ultra-smooth videos on the move with cameras for every situation.\r\nElevated interactions with moto ai*: Capture professional-level photos and videos and enjoy personalized assistance thanks to the seamless integration of moto ai*.\r\nVibrant 6.9\" ultrawide display: Immerse yourself in entertainment on the huge LTPO main display, experiencing a billion shades of color—with colors validated by Pantone***.', 0, NULL, 0, 'Motorola Razr 2025 | Unlocked | Made for US 8/256GB | 50MP Camera | Pantone Parfait Pink', '7000', 'Universal unlocked: Compatible with all major U.S. carriers, including Verizon, AT&T, T-Mobile and other prepaid carriers.', '2026-01-04 18:07:35.000000', NULL, 'ZMK', NULL, '1003931a-49b3-43e7-961a-9a9a3359e143_1.jpg,2adc76b7-d2ce-476a-86f9-e2fbed10e9ba_2.jpg,98512238-56c2-4503-b127-6667fcb9dd37_3.jpg,22f5fb73-559b-46aa-b670-6b3c65aa3166_4.jpg', NULL, 3);

-- --------------------------------------------------------

--
-- Table structure for table `product_images`
--

CREATE TABLE `product_images` (
  `id` bigint(20) NOT NULL,
  `image_path` varchar(255) NOT NULL,
  `product_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `store`
--

CREATE TABLE `store` (
  `id` bigint(20) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` text DEFAULT NULL,
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
  `durationtimeupdate` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `store`
--

INSERT INTO `store` (`id`, `active`, `created_at`, `description`, `email`, `name`, `storebanner`, `storeid`, `storelogo`, `updated_at`, `username`, `useridimage`, `contact_number`, `shopviews`, `whatsapp_number`, `numberofproducts`, `durationindays`, `durationtimeupdate`) VALUES
(1, b'1', '2025-12-19 00:21:41.000000', 'Black Space Online Store is your go-to digital marketplace for discovering a wide range of high-quality products at unbeatable prices. Our platform connects buyers and sellers seamlessly, offering a convenient, secure, and user-friendly shopping experience.\n\nAt Black Space, we are committed to providing exceptional customer service and ensuring every transaction is smooth and trustworthy. From electronics and fashion to home essentials and unique finds, we aim to bring the best products directly to your fingertips.\n\nOur mission is to create a space where shopping meets convenience, quality, and value, empowering both sellers to grow their businesses and buyers to discover products they love. With Black Space Online Store, your next purchase is just a click away.', 'abc@gmail.com', 'abc online store', 'banner.jpeg', 'BC_01', 'logo.jpeg', '2025-12-19 00:24:01.000000', '1001', 'logo.jpeg', '0973222222', '180', '0973297566', NULL, NULL, NULL),
(5, b'1', '2025-12-19 00:21:41.000000', 'Black Space Online Store is your go-to digital marketplace for discovering a wide range of high-quality products at unbeatable prices. Our platform connects buyers and sellers seamlessly, offering a convenient, secure, and user-friendly shopping experience.\r\n\r\nAt Black Space, we are committed to providing exceptional customer service and ensuring every transaction is smooth and trustworthy. From electronics and fashion to home essentials and unique finds, we aim to bring the best products directly to your fingertips.\r\n\r\nOur mission is to create a space where shopping meets convenience, quality, and value, empowering both sellers to grow their businesses and buyers to discover products they love. With Black Space Online Store, your next purchase is just a click away.', 'abc@gmail.com', 'abc online', 'banner.jpeg', 'BC_03', 'logo.jpeg', '2025-12-19 00:24:01.000000', '1002', 'logo.jpeg', '0973222222', '180', '09700000', NULL, NULL, NULL),
(6, b'1', '2025-12-17 00:21:49.000000', 'online salon', 'bb@gmail.com', 'bb onnli', 'banner.jpeg', 'BC_04', 'logo.jpeg', '2025-12-19 00:24:04.000000', '1003', 'logo.jpeg', '097111111', '120', '09700000', NULL, NULL, NULL),
(7, b'1', '2025-12-19 00:21:41.000000', 'Black Space Online Store is your go-to digital marketplace for discovering a wide range of high-quality products at unbeatable prices. Our platform connects buyers and sellers seamlessly, offering a convenient, secure, and user-friendly shopping experience.\r\n\r\nAt Black Space, we are committed to providing exceptional customer service and ensuring every transaction is smooth and trustworthy. From electronics and fashion to home essentials and unique finds, we aim to bring the best products directly to your fingertips.\r\n\r\nOur mission is to create a space where shopping meets convenience, quality, and value, empowering both sellers to grow their businesses and buyers to discover products they love. With Black Space Online Store, your next purchase is just a click away.', 'abc@gmail.com', 'abc online store 1 ', 'banner.jpeg', 'BC_05', 'logo.jpeg', '2025-12-19 00:24:01.000000', '1004', 'logo.jpeg', '0973222222', '180', '09700000', NULL, NULL, NULL),
(8, b'1', '2025-12-17 00:21:49.000000', 'online salon', 'bb@gmail.com', 'bb onnline store2', 'banner.jpeg', 'BC_06', 'logo.jpeg', '2025-12-19 00:24:04.000000', '1005', 'logo.jpeg', '097111111', '120', '09700000', NULL, NULL, NULL),
(9, b'1', '2025-12-19 00:21:41.000000', 'Black Space Online Store is your go-to digital marketplace for discovering a wide range of high-quality products at unbeatable prices. Our platform connects buyers and sellers seamlessly, offering a convenient, secure, and user-friendly shopping experience.\r\n\r\nAt Black Space, we are committed to providing exceptional customer service and ensuring every transaction is smooth and trustworthy. From electronics and fashion to home essentials and unique finds, we aim to bring the best products directly to your fingertips.\r\n\r\nOur mission is to create a space where shopping meets convenience, quality, and value, empowering both sellers to grow their businesses and buyers to discover products they love. With Black Space Online Store, your next purchase is just a click away.', 'abc@gmail.com', 'abc online3', 'banner.jpeg', 'BC_07', 'logo.jpeg', '2025-12-19 00:24:01.000000', '1006', 'logo.jpeg', '0973222222', '180', '09700000', NULL, NULL, NULL),
(10, b'1', '2025-12-17 00:21:49.000000', 'online salon', 'bb@gmail.com', 'bb onnli4', 'banner.jpeg', 'BC_08', 'logo.jpeg', '2025-12-19 00:24:04.000000', '1007', 'logo.jpeg', '097111111', '120', '09700000', NULL, NULL, NULL),
(11, b'1', '2025-12-19 00:21:41.000000', 'Black Space Online Store is your go-to digital marketplace for discovering a wide range of high-quality products at unbeatable prices. Our platform connects buyers and sellers seamlessly, offering a convenient, secure, and user-friendly shopping experience.\r\n\r\nAt Black Space, we are committed to providing exceptional customer service and ensuring every transaction is smooth and trustworthy. From electronics and fashion to home essentials and unique finds, we aim to bring the best products directly to your fingertips.\r\n\r\nOur mission is to create a space where shopping meets convenience, quality, and value, empowering both sellers to grow their businesses and buyers to discover products they love. With Black Space Online Store, your next purchase is just a click away.', 'abc@gmail.com', 'abc online store 1 22', 'banner.jpeg', 'BC_09', 'logo.jpeg', '2025-12-19 00:24:01.000000', '1008', 'logo.jpeg', '0973222222', '180', '09700000', NULL, NULL, NULL),
(12, b'1', '2025-12-17 00:21:49.000000', 'online salon', 'bb@gmail.com', 'bb onnline store223', 'banner.jpeg', 'BC_10', 'logo.jpeg', '2025-12-19 00:24:04.000000', '33/43/43', 'logo.jpeg', '097111111', '120', '09700000', NULL, NULL, NULL),
(13, b'1', '2025-12-19 00:21:41.000000', 'Black Space Online Store is your go-to digital marketplace for discovering a wide range of high-quality products at unbeatable prices. Our platform connects buyers and sellers seamlessly, offering a convenient, secure, and user-friendly shopping experience.\r\n\r\nAt Black Space, we are committed to providing exceptional customer service and ensuring every transaction is smooth and trustworthy. From electronics and fashion to home essentials and unique finds, we aim to bring the best products directly to your fingertips.\r\n\r\nOur mission is to create a space where shopping meets convenience, quality, and value, empowering both sellers to grow their businesses and buyers to discover products they love. With Black Space Online Store, your next purchase is just a click away.', 'abc@gmail.com', 'abc online324', 'banner.jpeg', 'BC_11', 'logo.jpeg', '2025-12-19 00:24:01.000000', '0498485', 'logo.jpeg', '0973222222', '180', '09700000', NULL, NULL, NULL),
(14, b'1', '2025-12-17 00:21:49.000000', 'online salon', 'bb@gmail.com', 'bb onnli425', 'banner.jpeg', 'BC_12', 'logo.jpeg', '2025-12-19 00:24:04.000000', '33/43/43', 'logo.jpeg', '097111111', '120', '09700000', NULL, NULL, NULL),
(15, b'1', '2025-12-19 00:21:41.000000', 'Black Space Online Store is your go-to digital marketplace for discovering a wide range of high-quality products at unbeatable prices. Our platform connects buyers and sellers seamlessly, offering a convenient, secure, and user-friendly shopping experience.\r\n\r\nAt Black Space, we are committed to providing exceptional customer service and ensuring every transaction is smooth and trustworthy. From electronics and fashion to home essentials and unique finds, we aim to bring the best products directly to your fingertips.\r\n\r\nOur mission is to create a space where shopping meets convenience, quality, and value, empowering both sellers to grow their businesses and buyers to discover products they love. With Black Space Online Store, your next purchase is just a click away.', 'abc@gmail.com', 'abc online31', 'banner.jpeg', 'BC_13', 'logo.jpeg', '2025-12-19 00:24:01.000000', '0498485', 'logo.jpeg', '0973222222', '180', '09700000', NULL, NULL, NULL),
(16, b'1', '2025-12-17 00:21:49.000000', 'online salon', 'bb@gmail.com', 'bb onnli41', 'banner.jpeg', 'BC_14', 'logo.jpeg', '2025-12-19 00:24:04.000000', '33/43/43', 'logo.jpeg', '097111111', '120', '09700000', NULL, NULL, NULL),
(17, b'1', '2025-12-19 00:21:41.000000', 'Black Space Online Store is your go-to digital marketplace for discovering a wide range of high-quality products at unbeatable prices. Our platform connects buyers and sellers seamlessly, offering a convenient, secure, and user-friendly shopping experience.\r\n\r\nAt Black Space, we are committed to providing exceptional customer service and ensuring every transaction is smooth and trustworthy. From electronics and fashion to home essentials and unique finds, we aim to bring the best products directly to your fingertips.\r\n\r\nOur mission is to create a space where shopping meets convenience, quality, and value, empowering both sellers to grow their businesses and buyers to discover products they love. With Black Space Online Store, your next purchase is just a click away.', 'abc@gmail.com', 'abc online store 1 221', 'banner.jpeg', 'BC_15', 'logo.jpeg', '2025-12-19 00:24:01.000000', '0498485', 'logo.jpeg', '0973222222', '180', '09700000', NULL, NULL, NULL),
(18, b'1', '2025-12-17 00:21:49.000000', 'online salon', 'bb@gmail.com', 'bb onnline store2231', 'banner.jpeg', 'BC_18', 'logo.jpeg', '2025-12-19 00:24:04.000000', '33/43/43', 'logo.jpeg', '097111111', '120', '09700000', NULL, NULL, NULL),
(19, b'1', '2025-12-19 00:21:41.000000', 'Black Space Online Store is your go-to digital marketplace for discovering a wide range of high-quality products at unbeatable prices. Our platform connects buyers and sellers seamlessly, offering a convenient, secure, and user-friendly shopping experience.\r\n\r\nAt Black Space, we are committed to providing exceptional customer service and ensuring every transaction is smooth and trustworthy. From electronics and fashion to home essentials and unique finds, we aim to bring the best products directly to your fingertips.\r\n\r\nOur mission is to create a space where shopping meets convenience, quality, and value, empowering both sellers to grow their businesses and buyers to discover products they love. With Black Space Online Store, your next purchase is just a click away.', 'abc@gmail.com', 'abc online3241', 'banner.jpeg', 'BC_17', 'logo.jpeg', '2025-12-19 00:24:01.000000', '0498485', 'logo.jpeg', '0973222222', '180', '09700000', NULL, NULL, NULL),
(24, b'1', '2025-12-24 15:03:04.000000', '1111111111', 'zulusteven9@gmail.com', '1111111stephen zulu', '1766581384547_Brochure-compliance.png', '1', '1766583385894_WhatsApp Image 2025-11-07 at 15.45.59.jpeg', '2025-12-24 15:36:25.000000', NULL, NULL, '0973297566', NULL, '11111', NULL, NULL, NULL),
(33, b'1', '2025-12-25 10:34:29.000000', 'aewewewe', 'zulusteven9@gmail.com', 'stephen zulu', '1766651669092_Brochure-compliance.png', '9', '1766651669091_2022-Annual-Report.png', NULL, NULL, NULL, '0973297566', NULL, '', NULL, NULL, NULL),
(34, b'1', '2025-12-25 10:34:45.000000', 'aewewewe', 'zulusteven9@gmail.com', 'stephen zulu', '1766651685756_Brochure-compliance.png', '8', '1766651685755_2022-Annual-Report.png', NULL, NULL, NULL, '0973297566', NULL, '', NULL, NULL, NULL),
(35, b'1', '2025-12-25 10:35:16.000000', 'aewewewe', 'zulusteven9@gmail.com', 'stephen zulu', '1766651716529_Brochure-compliance.png', '12', '1766651716527_2022-Annual-Report.png', NULL, NULL, NULL, '0973297566', NULL, '', NULL, NULL, NULL),
(36, b'1', '2025-12-25 10:35:34.000000', 'aewewewe', 'zulusteven9@gmail.com', 'stephen zulu', '1766651734324_Brochure-compliance.png', '11', '1766651734321_2022-Annual-Report.png', NULL, NULL, NULL, '0973297566', NULL, '', NULL, NULL, NULL),
(37, b'1', '2025-12-25 10:37:28.000000', '234567', 'zulusteven9@gmail.com', 'stephen zulu', '1766651848203_Strategic-Plan-2021-2025.png', '16', '1766651848198_2022-Annual-Report.png', NULL, NULL, NULL, '0973297566', NULL, '433535', NULL, NULL, NULL),
(38, b'1', '2025-12-25 10:39:33.000000', '45678', 'zulusteven9@gmail.com', 'stephen zulu', '1766651973443_2022-Annual-Report.png', '15', '1766651973442_2022-Annual-Report.png', NULL, NULL, NULL, '0973297566', NULL, '433535', NULL, NULL, NULL),
(39, b'1', '2025-12-25 10:54:59.000000', 'sedftgyhuijk', 'zulusteven9@gmail.com', 'stephen zulu', '1766652899449_Brochure-compliance.png', '14', '1766652899448_2022-Annual-Report.png', NULL, NULL, NULL, '0973297566', NULL, '1234', NULL, NULL, NULL),
(40, b'1', '2025-12-25 11:06:12.000000', 'werdtfygvbuhnjk', 'zulusteven9@gmail.com', 'stephen zulu', '1766653572543_WhatsApp Image 2025-11-07 at 15.45.59.jpeg', '13', '1766653572536_2022-Annual-Report.png', NULL, NULL, NULL, '0973297566', NULL, '', NULL, NULL, NULL),
(41, b'1', '2025-12-25 11:23:48.000000', 'wasedfghbjn', 'zulusteven9@gmail.com', 'stephen zulu', '1766654628660_2022-Annual-Report.png', '18', '1766654628653_2022-Annual-Report.png', NULL, NULL, NULL, '0973297566', NULL, '433535', NULL, NULL, NULL),
(42, b'1', '2025-12-25 11:25:24.000000', '23456yu', 'zulusteven9@gmail.com', 'stephen zulu', '1766654724371_Brochure-compliance.png', '17', '1766654724366_2022-Annual-Report.png', NULL, NULL, NULL, '0973297566', NULL, '123456', NULL, NULL, NULL),
(43, b'1', '2025-12-25 11:35:24.000000', 'werhtyj4567', 'zulusteven9@gmail.com', 'stephen zulu', '1766655324371_Strategic-Plan-2021-2025.png', '19', '1766655324365_2022-Annual-Report.png', NULL, NULL, NULL, '0973297566', NULL, '21345', NULL, NULL, NULL),
(44, b'1', '2025-12-25 11:49:36.000000', '2345678udfgcvhbj', 'zulusteven9@gmail.com', 'stephen zulu', '1766656176786_Brochure-compliance.png', 'STORE1766656176779', '1766656176779_2022-Annual-Report.png', NULL, 'sz1', NULL, '0973297566', NULL, '34567', NULL, NULL, NULL),
(45, b'1', '2025-12-25 11:56:40.000000', '2345rtygffhgfgh', 'ussd@workers.com.zm', 'ussd ussd', 'banner.jpeg', 'STORE20251225_1766656600855', '1766656600855_2022-Annual-Report.png', NULL, 'sz2', NULL, '0973297566', NULL, '213456', NULL, NULL, NULL),
(46, b'1', '2025-12-25 12:00:29.000000', '1234567812345678asdfghjasdfghj12345678asdfghj12345678asdfghj12345678asdfghj12345678asdfghj12345678asdfghj', 'zulusteven9@gmail.com', 'BC_01', '1766656829850_Strategic-Plan-2021-2025.png', 'STORE20251225_2984438', '1766656829845_2022-Annual-Report.png', '2026-01-01 01:17:44.000000', 'szulu', NULL, '0973297566', '122', '123456', '10', '3', '2025-01-05 01:13:18.000000');

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
(4, '800', 'Ideal For: Large stores wanting a full year subscription', 'Professional Plan', '365', '100');

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
(1, 'ndola zambia', 'zambia', '2025-12-23 20:55:06.000000', 'zulusteven99@gmail.com', b'1', 'stephen', 'zulu', '$2a$10$vSQexPanjStB83u.kj8szORk8mocCY7yrYwswhR2aDPUCoqwppwWG', '0973297567', 'SELLER', '1', 'ndola', '2025-12-23 20:55:06.000000', 'szulu1', NULL, '35434', '1001', NULL, NULL),
(15, 'ndola', 'Zambia', '2026-01-02 18:06:13.000000', 'zulusteven9@gmail.com', b'1', 'stephen ', 'zulu', '$2a$10$U4wyZIJu4dr0TK4ezHW.T.9awooqsrqQDvrvYDOs2XCkuC4hN8w7e', '0973297566', 'ADMIN', '1', 'ndola', '2026-01-03 12:44:28.000000', 'szulu', NULL, '35434', NULL, b'0', NULL),
(16, 'ndola', 'Zambia', '2026-01-03 19:00:48.000000', 'zulustev9@gmail.com', b'1', 'stephen', 'zulu', '$2a$10$6nAWxQEGXRvAvBNUktWjyOLPNs7ZIAYEJTLIbmR4YSCeGVnwrvjE6', '09732975', 'SELLER', '1', 'ndola', NULL, 'szulu2', NULL, '1232332', NULL, b'0', NULL);

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
-- Indexes for table `membership`
--
ALTER TABLE `membership`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKjnbg6epstwjw6ytgmxk6i3704` (`name`);

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
-- Indexes for table `product_images`
--
ALTER TABLE `product_images`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKp9v5ixrhq5e98xcdbcdbvvpn9` (`product_id`);

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
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `membership`
--
ALTER TABLE `membership`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `password_reset_token`
--
ALTER TABLE `password_reset_token`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `productstock`
--
ALTER TABLE `productstock`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT for table `product_images`
--
ALTER TABLE `product_images`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `store`
--
ALTER TABLE `store`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `subscription`
--
ALTER TABLE `subscription`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `password_reset_token`
--
ALTER TABLE `password_reset_token`
  ADD CONSTRAINT `FK5lwtbncug84d4ero33v3cfxvl` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `product_images`
--
ALTER TABLE `product_images`
  ADD CONSTRAINT `FKp9v5ixrhq5e98xcdbcdbvvpn9` FOREIGN KEY (`product_id`) REFERENCES `productstock` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
