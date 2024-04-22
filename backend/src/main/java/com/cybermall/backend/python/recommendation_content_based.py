import sys
import json
from collections import defaultdict
import random

def recommend_products(view_histories, all_products):
    """
    Recommends products based on user's view histories and all available products.

    Args:
        view_histories (list): A list of dictionaries representing the user's view histories.
            Each dictionary contains information about a viewed product, including 'product_id' and 'category'.
        all_products (list): A list of dictionaries representing all available products.
            Each dictionary contains information about a product, including 'product_id', 'category', 'number_of_view', and 'price'.

    Returns:
        list: A list of recommended product IDs, including both recommended and non-recommended products.
            The list is sorted based on the number of views and other criteria specified in the code.
    """
    # Collect categories viewed by the user
    user_categories = defaultdict(int)
    for history in view_histories:
        category = history['category']
        user_categories[category] += 1
    
    # Sort categories by view count
    sorted_categories = sorted(user_categories, key=user_categories.get, reverse=True)
    
    # Identify already viewed product IDs
    viewed_product_ids = {history['product_id'] for history in view_histories}
    
    # Separate products into recommended and non-recommended based on category and viewed status
    recommended = []
    not_recommended = []
    for product in all_products:
        if product['category'] in sorted_categories and product['product_id'] not in viewed_product_ids:
            recommended.append(product)
        elif product['product_id'] not in viewed_product_ids:
            not_recommended.append(product)

    # Sort recommended products by number of views (and any other criteria)
    recommended.sort(key=lambda p: (-p['number_of_view'], p['price']))
    
    # Sort non-recommended products by popularity (here, just number of views)
    not_recommended.sort(key=lambda p: -p['number_of_view'])

    # Collect product IDs from sorted lists
    recommended_ids = [p['product_id'] for p in recommended]
    not_recommended_ids = [p['product_id'] for p in not_recommended]

    # Collect viewed product IDs randomly
    viewed_ids = list(viewed_product_ids)
    random.shuffle(viewed_ids)  # Shuffle to randomize the order of viewed products

    # Combine all lists
    final_recommendation_list = recommended_ids + not_recommended_ids + viewed_ids
    
    return final_recommendation_list

if __name__ == "__main__":
    try:
        input_str = sys.stdin.read()
        input_data = json.loads(input_str)
        
        user_view_histories = input_data.get('user_view_histories', [])
        all_products = input_data.get('all_products', [])
        
        final_recommendation_ids = recommend_products(user_view_histories, all_products)
        
        print(json.dumps(final_recommendation_ids))
        
    except json.JSONDecodeError as e:
        print(f"Error parsing JSON input: {e}", file=sys.stderr)
        sys.exit(1)
