response = {'id': 'chatcmpl-9q7lzz48Woa84CdMw88K2O92Xgcnb', 'object': 'chat.completion', 'created': 1722209963, 'model': 'gpt-3.5-turbo-0125', 'choices': [{'index': 0, 'message': {'role': 'assistant', 'content': "- le chapitre\n- reprendre\n- le son\n- le souffle\n- la sportive\n- la sérieuse\n- ## Puis\n- lever\n- les yeux\n- jurer\n- commencer\n- une activité\n- P.N. Katell\n- P.N. Guénan\n- P.N. Fabrice\n- P.N. Lami\n- ## discutent\n- ## devant\n- l'église\n- NotreDame\n- les chats\n- ## lisent\n- aussi\n- ## dans\n- ## esprit\n- le commissaire\n- est\n- rassuré\n- car\n- n'a\n- pas\n- quitté\n- veut\n- s'avancer\n- pour\n- lui\n- parler\n- mais\n- décide\n- observer\n- la scène\n- la jeune femme\n- écouter"}, 'logprobs': None, 'finish_reason': 'stop'}], 'usage': {'prompt_tokens': 309, 'completion_tokens': 197, 'total_tokens': 506}, 'system_fingerprint': None}

print(response["choices"][0]["message"]["content"])

def parse_gpt(gpt_response):
    return gpt_response["choices"][0]["message"]["content"][2:].split("\n- ")

print(parse_gpt(response))