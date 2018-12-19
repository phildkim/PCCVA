-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 19, 2018 at 07:24 AM
-- Server version: 10.1.36-MariaDB
-- PHP Version: 7.2.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pcc`
--

-- --------------------------------------------------------

--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `id` varchar(8) NOT NULL,
  `name` varchar(100) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admins`
--

INSERT INTO `admins` (`id`, `name`, `username`, `password`) VALUES
('58496858', 'Carrie Starbird', 'carrie', '$2y$10$h6ni.5L9ge.GXf6r/Ojm2.arUAXsNomNWh361xmZF/NhWTz/5AuPS'),
('67767249', 'Jamal Ashraf', 'ash', '$2y$10$iPproWmaIqsqNazYWD3rKezgTMk8LV5/bRYMqFAcl1RLKWzUzRhLi'),
('94647975', 'Dr. Rajen Vurdien', 'raj', '$2y$10$NsJDwoPY21RbJgLVfqCWOea2zBAz1/kzTmGDYgGsUudJJVxOx569G'),
('99690390', 'Juan Leon', 'leo', '$2y$10$CyA5MpcKiVbR7IK/7mOT9OUattWlUDcrBE6y.zJWHTfTFk4wa1eXC');

-- --------------------------------------------------------

--
-- Table structure for table `faculties`
--

CREATE TABLE `faculties` (
  `id` int(8) NOT NULL,
  `name` varchar(100) NOT NULL,
  `title` varchar(100) NOT NULL,
  `phone` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `faculties`
--

INSERT INTO `faculties` (`id`, `name`, `title`, `phone`, `email`) VALUES
(1, 'Ashraf, Jamal', 'Assistant Professor', '(626) 585-3095', 'jjashraf@pasadena.edu'),
(2, 'Barkeshli, Sassan', 'Assistant Professor', '(626) 585-7538', 'sxbarkeshli@pasadena.edu'),
(3, 'Chang, Keng', 'Adjunct Faculty', '(626) 585-7331', 'kchang27@pasadena.edu'),
(4, 'Jupe, Erion', 'Adjunct Faculty', '(626) 585-7331', 'ejupe@pasadena.edu'),
(5, 'Kutukian, Garen', 'Adjunct Faculty', '(626) 585-7331', 'gkutukian@pasadena.edu'),
(6, 'Leon, Juan', 'Instructor', '(626) 585-3262', 'jleon28@pasadena.edu'),
(7, 'Noguchi, Alexander', 'Adjunct Faculty', '(626) 585-1111', 'anoguchi@pasadena.edu'),
(8, 'Ravinutala, Sekhar', 'Adjunct Faculty', '(626) 585-2222', 'sravinutala1@pasadena.edu'),
(9, 'Wilkinson, Paul', 'Professor', '(626) 585-7578', 'pkwilkinson@pasadena.edu');

-- --------------------------------------------------------

--
-- Table structure for table `get_appointments`
--

CREATE TABLE `get_appointments` (
  `sid` int(8) NOT NULL,
  `name` varchar(100) NOT NULL,
  `dean` varchar(100) NOT NULL,
  `date` varchar(11) NOT NULL,
  `time` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `get_appointments`
--

INSERT INTO `get_appointments` (`sid`, `name`, `dean`, `date`, `time`) VALUES
(118, 'Philip Kim', 'Dean: Carrie Starbird', '12/16/2018', '2:00 PM - 2:20 PM');

-- --------------------------------------------------------

--
-- Table structure for table `set_appointments`
--

CREATE TABLE `set_appointments` (
  `app_id` int(8) NOT NULL,
  `id` varchar(8) NOT NULL,
  `name` varchar(100) NOT NULL,
  `date` varchar(10) NOT NULL,
  `stime` varchar(11) NOT NULL,
  `etime` varchar(11) NOT NULL,
  `times` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `set_appointments`
--

INSERT INTO `set_appointments` (`app_id`, `id`, `name`, `date`, `stime`, `etime`, `times`) VALUES
(72, '58496858', 'Carrie Starbird', '12/16/2018', '08:00 AM', '05:00 PM', '8:00 AM - 8:20 AM;8:30 AM - 8:50 AM;9:00 AM - 9:20 AM;9:30 AM - 9:50 AM;10:00 AM - 10:20 AM;10:30 AM - 10:50 AM;11:00 AM - 11:20 AM;LUNCH BREAK;LUNCH BREAK;1:30 PM - 1:50 PM;2:30 PM - 2:50 PM;3:00 PM - 3:20 PM;3:30 PM - 3:50 PM;4:00 PM - 4:20 PM;4:30 PM - 4:50 PM;'),
(73, '58496858', 'Carrie Starbird', '12/17/2018', '08:00 AM', '06:00 PM', '8:00 AM - 8:20 AM;8:30 AM - 8:50 AM;9:00 AM - 9:20 AM;9:30 AM - 9:50 AM;10:00 AM - 10:20 AM;10:30 AM - 10:50 AM;11:00 AM - 11:20 AM;LUNCH BREAK;LUNCH BREAK;1:00 PM - 1:20 PM;1:30 PM - 1:50 PM;2:00 PM - 2:20 PM;2:30 PM - 2:50 PM;3:00 PM - 3:20 PM;3:30 PM - 3:50 PM;4:00 PM - 4:20 PM;4:30 PM - 4:50 PM;5:00 PM - 5:20 PM;5:30 PM - 5:50 PM;');

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `id` varchar(8) NOT NULL,
  `name` varchar(100) NOT NULL,
  `username` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `password` text NOT NULL,
  `major` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`id`, `name`, `username`, `email`, `phone`, `password`, `major`) VALUES
('26263085', 'test', 'test', 'test', '1(626) 476-5336', '$2y$10$qE9NXNjLZROiOIDrIUZpsuiezTFvwsclyDOrLqhNfaEADRDHyi.2a', 'business'),
('32453568', 'Philip Kim', 'pkim', 'pkim@go.pasadena.edu', '1(818) 669-8451', '$2y$10$DR1MNwVWASWwTM0ZsDPdde2w2mhqw3pMfv1jVpqxoCU2diz1VVgzm', 'Computer Science'),
('86650734', 'Wilson Gan', 'wgan', 'wgan@go.pasadena', '1(626) 123-3232', '$2y$10$bS2gDfBfl4BFmFmtFLM/7uXJN.YEvZB3vcxy6TIzMwi.n2.6Z18ru', 'Computer Science');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `faculties`
--
ALTER TABLE `faculties`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `get_appointments`
--
ALTER TABLE `get_appointments`
  ADD PRIMARY KEY (`sid`);

--
-- Indexes for table `set_appointments`
--
ALTER TABLE `set_appointments`
  ADD PRIMARY KEY (`app_id`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `faculties`
--
ALTER TABLE `faculties`
  MODIFY `id` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `get_appointments`
--
ALTER TABLE `get_appointments`
  MODIFY `sid` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=119;

--
-- AUTO_INCREMENT for table `set_appointments`
--
ALTER TABLE `set_appointments`
  MODIFY `app_id` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=74;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
