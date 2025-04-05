const express = require('express');
const { pipeline } = require('@xenova/transformers');

const app = express();
app.use(express.json());

const loadedModels = {};

async function getTranslator(sourceLang, targetLang) {
  const modelKey = `${sourceLang}-${targetLang}`;
  
  if (!loadedModels[modelKey]) {
    const modelName = `Helsinki-NLP/opus-mt-${sourceLang}-${targetLang}`;
    
    try {
      const translator = await pipeline('translation', modelName);
      loadedModels[modelKey] = translator;
      console.log(`Loaded model: ${modelName}`);
    } catch (e) {
      console.error(`Error loading model: ${e}`);
      return null;
    }
  }
  
  return loadedModels[modelKey];
}

app.post('/translate', async (req, res) => {
  const { source_lang = 'en', target_lang = 'fr', text = '' } = req.body;
  
  if (!text) {
    return res.status(400).json({ error: "No text provided" });
  }
  
  try {
    const translator = await getTranslator(source_lang, target_lang);
    if (!translator) {
      return res.status(404).json({ 
        error: `Translation model for ${source_lang} to ${target_lang} not available` 
      });
    }
    
    const result = await translator(text, {
      max_length: 100,
      num_beams: 4,
      no_repeat_ngram_size: 3,
      early_stopping: true
    });
    
    return res.json({ translated_text: result[0].translation_text });
  } catch (e) {
    return res.status(500).json({ error: `Translation error: ${e.message}` });
  }
});

app.get('/available_languages', (req, res) => {
  const commonTargets = {
    "fr": "French",
    "es": "Spanish",
    "de": "German",
    "it": "Italian",
    "pt": "Portuguese",
    "ru": "Russian",
    "zh": "Chinese",
    "ar": "Arabic",
    "ja": "Japanese",
    "ko": "Korean",
    "pl": "Polish",
    "ro": "Romanian",
    "bs": "Bosnian"
  };
  
  return res.json(commonTargets);
});

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => {
  console.log(`Starting translation server on port ${PORT}...`);
});