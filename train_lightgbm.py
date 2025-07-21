import numpy as np
from lightgbm import LGBMRegressor
from onnxmltools.convert import convert_lightgbm
from onnxmltools.utils import save_model
from skl2onnx.common.data_types import FloatTensorType

# Generate a simple regression dataset
rng = np.random.default_rng(0)
X = rng.random((200, 5), dtype=np.float32)
y = X.sum(axis=1) + 0.1 * rng.standard_normal(200)

# Train LightGBM model
model = LGBMRegressor(n_estimators=20)
model.fit(X, y)

# Convert to ONNX
initial_types = [('input', FloatTensorType([None, 5]))]
onnx_model = convert_lightgbm(model, initial_types=initial_types)

# Save to resources directory
import os
os.makedirs('src/main/resources', exist_ok=True)
save_model(onnx_model, 'src/main/resources/model.onnx')
print('Saved ONNX model to src/main/resources/model.onnx')
