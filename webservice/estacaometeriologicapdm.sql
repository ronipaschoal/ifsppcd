-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: 05-Dez-2018 às 22:13
-- Versão do servidor: 5.7.21
-- PHP Version: 7.1.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `estacaometeriologicapdm`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `estacao`
--

CREATE TABLE `estacao` (
  `id` int(11) NOT NULL,
  `nome` varchar(75) NOT NULL,
  `tempoLeitura` int(11) NOT NULL DEFAULT '5',
  `ativo` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `estacao`
--

INSERT INTO `estacao` (`id`, `nome`, `tempoLeitura`, `ativo`) VALUES
(1, 'ESTACAO FIXA', 5000, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `historico`
--

CREATE TABLE `historico` (
  `id` int(11) NOT NULL,
  `idLeitura` int(11) NOT NULL,
  `valor` varchar(255) NOT NULL,
  `data` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estrutura da tabela `leitura`
--

CREATE TABLE `leitura` (
  `id` int(11) NOT NULL,
  `idEstacao` int(11) NOT NULL,
  `idSensor` int(11) NOT NULL,
  `codigo` varchar(10) NOT NULL,
  `nome` varchar(75) NOT NULL,
  `unidadeMedida` varchar(15) NOT NULL,
  `ativo` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `leitura`
--

INSERT INTO `leitura` (`id`, `idEstacao`, `idSensor`, `codigo`, `nome`, `unidadeMedida`, `ativo`) VALUES
(1, 1, 1, 'FIX-01', 'TEMPERATURA', '°C', 1),
(2, 1, 1, 'FIX-02', 'UMIDADE', '%', 1),
(3, 1, 2, 'FIX-03', 'FLUXO DE AGUA', 'L/min', 1),
(4, 1, 3, 'FIX-04', 'CHAMA', '', 1),
(5, 1, 4, 'FIX-05', 'VIBRACAO', '', 1),
(6, 1, 5, 'FIX-06', 'UMIDADE SOLO', '', 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `sensor`
--

CREATE TABLE `sensor` (
  `id` int(11) NOT NULL,
  `nome` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `sensor`
--

INSERT INTO `sensor` (`id`, `nome`) VALUES
(1, 'DHT22'),
(2, 'YF-S201B'),
(3, 'CHAMA'),
(4, 'SW-420'),
(5, 'HIGROMETRO');

-- --------------------------------------------------------

--
-- Estrutura da tabela `temporeal`
--

CREATE TABLE `temporeal` (
  `id` int(11) NOT NULL,
  `idLeitura` int(11) NOT NULL,
  `valor` varchar(255) NOT NULL,
  `data` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `temporeal`
--

INSERT INTO `temporeal` (`id`, `idLeitura`, `valor`, `data`) VALUES
(1, 1, '25.50', '2018-12-04 01:29:54'),
(5, 2, '63.60', '2018-12-04 01:29:57'),
(6, 3, '0.00', '2018-12-04 01:29:59'),
(7, 4, 'FOGO DETECTADO', '2018-12-04 01:30:01'),
(8, 5, 'VIBRACAO BAIXA', '2018-12-04 01:30:03'),
(11, 6, 'UMIDO', '2018-12-04 01:30:05');

-- --------------------------------------------------------

--
-- Estrutura da tabela `usuario`
--

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL,
  `idEstacao` int(11) NOT NULL,
  `login` varchar(30) NOT NULL,
  `senha` varchar(15) NOT NULL,
  `nome` varchar(75) NOT NULL,
  `ativo` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `estacao`
--
ALTER TABLE `estacao`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `historico`
--
ALTER TABLE `historico`
  ADD PRIMARY KEY (`id`),
  ADD KEY `IX_historico_idLeitura` (`idLeitura`);

--
-- Indexes for table `leitura`
--
ALTER TABLE `leitura`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `codigo` (`codigo`),
  ADD KEY `IX_leitura_idEstacao` (`idEstacao`),
  ADD KEY `IX_leitura_idSensor` (`idSensor`);

--
-- Indexes for table `sensor`
--
ALTER TABLE `sensor`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `temporeal`
--
ALTER TABLE `temporeal`
  ADD PRIMARY KEY (`id`),
  ADD KEY `IX_tempoReal_idLeitura` (`idLeitura`);

--
-- Indexes for table `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_usuario_idEstacao_login` (`idEstacao`,`login`),
  ADD KEY `IX_usuario_idEstacao` (`idEstacao`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `estacao`
--
ALTER TABLE `estacao`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `historico`
--
ALTER TABLE `historico`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `leitura`
--
ALTER TABLE `leitura`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `sensor`
--
ALTER TABLE `sensor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `temporeal`
--
ALTER TABLE `temporeal`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `historico`
--
ALTER TABLE `historico`
  ADD CONSTRAINT `FK_historico_leitura_idLeitura` FOREIGN KEY (`idLeitura`) REFERENCES `leitura` (`id`);

--
-- Limitadores para a tabela `leitura`
--
ALTER TABLE `leitura`
  ADD CONSTRAINT `FK_leitura_estacao_idEstacao` FOREIGN KEY (`idEstacao`) REFERENCES `estacao` (`id`),
  ADD CONSTRAINT `FK_leitura_sensor_idSensor` FOREIGN KEY (`idSensor`) REFERENCES `sensor` (`id`);

--
-- Limitadores para a tabela `temporeal`
--
ALTER TABLE `temporeal`
  ADD CONSTRAINT `FK_tempoReal_leitura_idLeitura` FOREIGN KEY (`idLeitura`) REFERENCES `leitura` (`id`);

--
-- Limitadores para a tabela `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `FK_usuario_estacao_idEstacao` FOREIGN KEY (`idEstacao`) REFERENCES `estacao` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
