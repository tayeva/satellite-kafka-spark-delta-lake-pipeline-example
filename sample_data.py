#!/usr/bin/env python

"""
Generator satellite sample data
"""

from argparse import ArgumentParser, ArgumentDefaultsHelpFormatter, Namespace
from pathlib import Path
from typing import Dict, Generator
import logging

from sklearn.preprocessing import MinMaxScaler
import numpy as np
import pandas as pd


LOGGER = logging.getLogger(__name__)

TEMP_LOW: int = -100
TEMP_HIGH: int = 120

BATTERY_CHARGE_LOW: int = 0
BATTERY_CHARGE_HIGH: int = 100

ALTIUDE_KM_LOW: int = 160
ALTIUDE_KM_HIGH: int = 2000


def _command_line_interface() -> Namespace:
    parser = ArgumentParser(
        "Samples script -- a script for generating samples.",
        formatter_class=ArgumentDefaultsHelpFormatter,
    )
    parser.add_argument("directory", help="The directory to generate the samples to.")
    parser.add_argument(
        "-n",
        "--n-satellites",
        default=1000,
        type=int,
        help="The number of satellites to generate data for.",
    )
    parser.add_argument("-s", "--seed", default=88, type=int, help="Random seed")
    parser.add_argument(
        "--samples", default=1000, type=int, help="Number of samples to generate."
    )
    return parser.parse_args()


def sample(n_samples: int) -> pd.DataFrame:
    """
    Create a dataframe of satellite sample data.

    Fields - Temperature (C), Battery charge percent, altitude,
        sensor 1, sensor 2, sensor 3

    n_samples - the number of samples to generate
    """
    df = pd.DataFrame()
    df["temp_c"] = MinMaxScaler(feature_range=(TEMP_LOW, TEMP_HIGH)).fit_transform(
        (
            np.sin(np.linspace(0, np.random.randint(10, 20), n_samples))
            + np.random.uniform() * np.random.randn(n_samples)
        ).reshape(-1, 1)
    )[:, 0]
    df["battery_charge_pct"] = MinMaxScaler(
        feature_range=(BATTERY_CHARGE_LOW, BATTERY_CHARGE_HIGH)
    ).fit_transform(
        (
            np.sin(np.linspace(0, np.random.randint(10, 20), n_samples))
            + np.random.uniform() * np.random.randn(n_samples)
        ).reshape(-1, 1)
    )[
        :, 0
    ]
    df["altitude"] = np.random.uniform(
        low=ALTIUDE_KM_LOW, high=ALTIUDE_KM_HIGH, size=(n_samples, 1)
    )
    df["sensor1"] = MinMaxScaler().fit_transform(
        (
            np.sin(np.linspace(0, np.random.randint(10, 100), n_samples))
            + 0.1 * np.random.randn(n_samples)
        ).reshape(-1, 1)
    )[:, 0]
    df["sensor2"] = MinMaxScaler().fit_transform(
        (
            np.sin(np.linspace(0, np.random.randint(10, 100), n_samples))
            + 0.1 * np.random.randn(n_samples)
        ).reshape(-1, 1)
    )[:, 0]
    df["sensor3"] = MinMaxScaler().fit_transform(
        (
            np.sin(np.linspace(0, np.random.randint(10, 100), n_samples))
            + 0.1 * np.random.randn(n_samples)
        ).reshape(-1, 1)
    )[:, 0]
    return df.round(2)


def samples(
    seed: int, n_satellites: int, n_samples: int
) -> Dict[str, pd.DataFrame]:
    """
    Create samples for n 'satellites'
    """
    np.random.seed(seed)
    LOGGER.info("Seed:%s", seed)
    return {f"sat{i}": sample(n_samples) for i in range(n_satellites)}


def write_to_csv(
    samples: Dict[str, pd.DataFrame], directory: Path
) -> Generator[Path, None, None]:
    """
    Write satellite samples to disk (csv).

    Returns a generator of paths.
    """
    if not directory.exists():
        directory.mkdir()
        LOGGER.info("Created directory:%s", directory)
    for sat_name, sat_data in samples.items():
        file_path = directory.joinpath(f"{sat_name}.csv")
        sat_data.to_csv(file_path, index=False)
        yield file_path
        LOGGER.info("Wrote:%s", file_path)


def main() -> None:
    """Satellite samples script"""
    logging.basicConfig(level=logging.INFO)
    LOGGER.info("Satellite sample data gen.")
    args = _command_line_interface()
    output_dir = Path(args.directory)
    if output_dir.exists():
        LOGGER.info("Output directory exists, not doing anything: %s", output_dir)
        return None
    _samples = samples(args.seed, args.n_satellites, args.samples)
    paths = list(write_to_csv(_samples, output_dir))
    LOGGER.info("Wrote %s files to %s", len(paths), args.directory)


if __name__ == "__main__":
    main()
